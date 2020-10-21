package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.constant.EventBusTag;
import com.android.dsly.rxhttp.utils.GzipUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.bumptech.glide.Glide;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.bean.CallBean;
import com.htxtdshopping.htxtd.frame.databinding.ActivityVoiceBinding;
import com.htxtdshopping.htxtd.frame.event.VoiceHangUpEvent;
import com.htxtdshopping.htxtd.frame.ui.third.viewmodel.VoiceViewModel;
import com.htxtdshopping.htxtd.frame.utils.AudioConsumer;
import com.htxtdshopping.htxtd.frame.utils.AudioConsumerData;
import com.htxtdshopping.htxtd.frame.utils.AudioProducer;
import com.htxtdshopping.htxtd.frame.utils.VoicePlayManager;
import com.htxtdshopping.htxtd.frame.utils.udp.UdpClient;
import com.jeremyliao.liveeventbus.LiveEventBus;

import androidx.lifecycle.Observer;

public class VoiceActivity extends BaseActivity<ActivityVoiceBinding, VoiceViewModel> implements AudioProducer.OnDataListener, View.OnClickListener {

    //未接听
    private static final String MISSED = "MISSED";
    //接听中
    private static final String ANSWERING = "ANSWERING";

    private UdpClient mUdpClient;

    private String mToUserId;
    private String mToUserHeadUrl;
    private String mToUserName;

    //接听状态
    private String mStatus;

    //铃声播放类
    private VoicePlayManager mPlayManager;

    public static void start(Context context, String toUserId, String toUserHeadUrl, String toUserName,
                             String roomId, String voiceUserId) {
        boolean activityExists = ActivityUtils.isActivityExistsInStack(VoiceActivity.class);
        if (activityExists) {
            return;
        }
        Intent intent = new Intent(context, VoiceActivity.class);
        intent.putExtra("toUserId", toUserId);
        intent.putExtra("toUserHeadUrl", toUserHeadUrl);
        intent.putExtra("toUserName", toUserName);
        intent.putExtra("roomId", roomId);
        intent.putExtra("voiceUserId", voiceUserId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return R.layout.activity_voice;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setOnClickListener(this);

        mPlayManager = VoicePlayManager.getInstance().isLooping(true);
    }

    @Override
    public void initEvent() {
        mViewModel.getCallLiveData().observe(this, new Observer<CallBean>() {
            @Override
            public void onChanged(CallBean data) {
                startVoice(data.getRoomId(), data.getUserId());
            }
        });
        mViewModel.getHangUpLiveData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                //取消震动和铃声
                VibrateUtils.cancel();
                mPlayManager.stopPlay();

                finish();
            }
        });
        LiveEventBus.get(EventBusTag.EVENT_SEND_MESSAGE, VoiceHangUpEvent.class).observe(this, new Observer<VoiceHangUpEvent>() {
            @Override
            public void onChanged(VoiceHangUpEvent event) {
                if (!mViewModel.getRoomId().equals(event.getRoomId())) {
                    return;
                }
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_refuse) {
            mViewModel.hangUp(mToUserId);
        } else if (v.getId() == R.id.ll_answer) {
            //取消震动和铃声
            VibrateUtils.cancel();
            mPlayManager.stopPlay();

            changeStatus(ANSWERING);
            startVoice(mViewModel.getRoomId(), mViewModel.getVoiceUserId());
        } else if (v.getId() == R.id.ll_hangUp) {
            mViewModel.hangUp(mToUserId);
        } else if (v.getId() == R.id.ll_handsfree) {
            AudioConsumer.getInstance().setSpeakerphoneOn(!AudioConsumer.getInstance().isSpeakerphoneOn());
            if (AudioConsumer.getInstance().isSpeakerphoneOn()) {
                mBinding.ivHandsfree.setImageResource(R.drawable.icon_turn_off_speaker);
            } else {
                mBinding.ivHandsfree.setImageResource(R.drawable.icon_turn_on_speaker);
            }
        }
    }

    @Override
    public void initData() {
        String roomId = getIntent().getStringExtra("roomId");
        String voiceUserId = getIntent().getStringExtra("voiceUserId");
        mToUserId = getIntent().getStringExtra("toUserId");
        mToUserHeadUrl = getIntent().getStringExtra("toUserHeadUrl");
        mToUserName = getIntent().getStringExtra("toUserName");
        mBinding.tvName.setText(mToUserName);
        Glide.with(this).load(mToUserHeadUrl).into(mBinding.ivHead);

        mUdpClient = new UdpClient() {

            @Override
            protected void startCall() {
                mViewModel.cancelCall();

                mBinding.tvCallTxt.post(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.tvCallTxt.setText("开始通话");
                    }
                });
            }

            @Override
            protected void listen(String str) {
                byte[] decode = ConvertUtils.hexString2Bytes(str);
                byte[] voiceData = GzipUtils.uncompress(decode);
                AudioConsumerData.getInstance().put(voiceData);
            }

            @Override
            protected void callWait() {
                mBinding.tvCallTxt.post(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.tvCallTxt.setText("等待接通");
                    }
                });
            }
        };
        AudioProducer.getInstance().setOnDataListener(this);

        if (TextUtils.isEmpty(roomId) || TextUtils.isEmpty(voiceUserId)) {
            changeStatus(ANSWERING);
            mViewModel.call(mToUserId);
        } else {
            changeStatus(MISSED);
            mViewModel.setRoomId(roomId);
            mViewModel.setVoiceUserId(voiceUserId);

            //开始播放铃声
            Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mPlayManager.startPlay(defaultUri.getPath());
            //震动
            VibrateUtils.vibrate(new long[]{600, 800}, 0);
        }
    }

    private void changeStatus(String status) {
        mStatus = status;
        if (status.equals(MISSED)) {
            mBinding.llMissed.setVisibility(View.VISIBLE);
            mBinding.llAnswering.setVisibility(View.GONE);
        } else if (status.equals(ANSWERING)) {
            mBinding.llMissed.setVisibility(View.GONE);
            mBinding.llAnswering.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onVoiceData(String data) {
        if (mUdpClient.getStatus() == 2) {
            try {
                mUdpClient.submitStream(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //开始接听语音
    public void startVoice(String roomId, String voiceUserId) {
        mUdpClient.start("127.0.0.1", 10001, 10005, roomId, voiceUserId);

        AudioConsumer.getInstance().start();
        AudioProducer.getInstance().start();
    }

    @Override
    protected void onDestroy() {
        AudioProducer.getInstance().stop();
        AudioConsumer.getInstance().stop();

        if (mUdpClient != null) {
            mUdpClient.close();
        }

        VibrateUtils.cancel();
        mPlayManager.stopPlay();
        mPlayManager = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected boolean isFitWindow() {
        return false;
    }
}