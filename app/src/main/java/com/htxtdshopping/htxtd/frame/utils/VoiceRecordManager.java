package com.htxtdshopping.htxtd.frame.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 陈志鹏
 * @date 2018/11/9
 */
public class VoiceRecordManager implements Handler.Callback {
    /**
     * 录音临时文件路径
     */
    public static String TEMP_FILE_PATH;
    /**
     * 正式录音文件路径
     */
    public static String FILE_PATH;
    /**
     * 录音最长时间（单位：秒）
     */
    private int RECORD_INTERVAL;
    private IAudioState mCurAudioState;
    private Context mContext;
    private Handler mHandler;
    private AudioManager mAudioManager;
    private MediaRecorder mMediaRecorder;
    private long mStartRecTime;
    private long mSumRecTime;
    /**
     * 临时录音文件
     */
    private File mTempFile;
    /**
     * 临时录音文件集合
     */
    private ArrayList<File> mRecList = new ArrayList<File>();
    private AudioManager.OnAudioFocusChangeListener mAfChangeListener;
    private OnAudioRecordListener mListener;
    IAudioState idleState;
    IAudioState recordState;
    IAudioState pauseState;

    public static VoiceRecordManager getInstance() {
        return VoiceRecordManager.SingletonHolder.sInstance;
    }

    private VoiceRecordManager() {
        this.RECORD_INTERVAL = 60;
        mContext = Utils.getApp();
        TEMP_FILE_PATH = mContext.getFilesDir().getAbsolutePath() + "/tempVoiceRecord";
        FileUtils.createOrExistsDir(TEMP_FILE_PATH);
        FILE_PATH = mContext.getFilesDir().getAbsolutePath() + "/voiceRecord";
        FileUtils.createOrExistsDir(FILE_PATH);
        this.idleState = new VoiceRecordManager.IdleState();
        this.recordState = new VoiceRecordManager.RecordState();
        pauseState = new VoiceRecordManager.PauseState();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                TelephonyManager manager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
                manager.listen(new PhoneStateListener() {
                    @Override
                    public void onCallStateChanged(int state, String incomingNumber) {
                        switch (state) {
                            case TelephonyManager.APPTYPE_SIM:
                                VoiceRecordManager.this.sendMessage(6, null);
                                break;
                            case TelephonyManager.APPTYPE_USIM:
                            default:
                                super.onCallStateChanged(state, incomingNumber);
                                break;
                        }
                    }
                }, 32);
            } catch (SecurityException var2) {
                var2.printStackTrace();
            }
        }

        this.mCurAudioState = this.idleState;
        this.idleState.enter();
    }

    @Override
    public final boolean handleMessage(Message msg) {
        sendMessage(msg.what, msg.obj);
        return false;
    }

    private void removeMessages() {
        this.mHandler.removeMessages(2);
    }

    public void setMaxVoiceDuration(int maxVoiceDuration) {
        this.RECORD_INTERVAL = maxVoiceDuration;
    }

    public int getMaxVoiceDuration() {
        return this.RECORD_INTERVAL;
    }

    /**
     * 开始录音
     */
    public void start() {
        start(true);
    }

    /**
     * 开始录音（内部调）
     */
    private void start(boolean init) {
        VoicePlayManager.getInstance().stopPlay();
        if (mCurAudioState == recordState) {
            return;
        }
        if (init) {
            mSumRecTime = 0;
            deleteTempFiles();
        }
        this.mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        if (this.mAfChangeListener != null) {
            this.mAudioManager.abandonAudioFocus(this.mAfChangeListener);
            this.mAfChangeListener = null;
        }

        this.mAfChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    VoiceRecordManager.this.mAudioManager.abandonAudioFocus(VoiceRecordManager.this.mAfChangeListener);
                    VoiceRecordManager.this.mAfChangeListener = null;
                    VoiceRecordManager.this.sendMessage(6, null);
                }
            }
        };
        if (init) {
            sendMessage(1, null);
        } else {
            sendMessage(4, null);
        }
    }

    /**
     * 暂停录音
     */
    public void pause() {
        sendMessage(3, null);
    }

    /**
     * 继续录音
     */
    public void resume() {
        start(false);
    }

    /**
     * 录音结束
     */
    public void stop() {
        sendMessage(5, null);
    }

    /**
     * 删除录音
     */
    public void destroyRecord() {
        sendMessage(6, null);
    }

    void sendMessage(int event, Object obj) {
        Message message = Message.obtain();
        message.what = event;
        message.obj = obj;
        this.mCurAudioState.handleMessage(message);
    }

    private void startRec() {
        try {
            this.muteAudioFocus(this.mAudioManager, true);
            //设置声音模式
            this.mAudioManager.setMode(AudioManager.MODE_NORMAL);
            this.mMediaRecorder = new MediaRecorder();
            //设置录制的音频采样率
            this.mMediaRecorder.setAudioSamplingRate(8000);
            //设置录制的音频编码比特率
            this.mMediaRecorder.setAudioEncodingBitRate(7950);
            //设置录制的音频通道数
            this.mMediaRecorder.setAudioChannels(1);
            //设置音频源
            this.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置在录制过程中产生的输出文件的格式
            this.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            //设置audio的编码格式，只有设置成aac苹果端才能播放android的录音文件
            this.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mTempFile = new File(TEMP_FILE_PATH, System.currentTimeMillis() + "temp.voice");
            FileUtils.createOrExistsFile(mTempFile);
            //设置输出文件的路径
            this.mMediaRecorder.setOutputFile(mTempFile.getAbsolutePath());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private boolean checkAudioTimeLength() {
        return mSumRecTime < 1000L;
    }

    private void stopRec() {
        try {
            this.muteAudioFocus(this.mAudioManager, false);
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.reset();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    private void calculateSumTime() {
        if (this.mTempFile != null) {
            if (!mTempFile.exists() || mTempFile.length() == 0L) {
                return;
            }
            mRecList.add(mTempFile);
            mSumRecTime += SystemClock.elapsedRealtime() - this.mStartRecTime;
        }
    }

    private void muteAudioFocus(AudioManager audioManager, boolean bMute) {
        if (bMute) {
            audioManager.requestAudioFocus(this.mAfChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        } else {
            audioManager.abandonAudioFocus(this.mAfChangeListener);
            this.mAfChangeListener = null;
        }
    }

    class RecordState extends IAudioState {

        @Override
        void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    if (mMediaRecorder != null) {
                        int maxAmplitude = mMediaRecorder.getMaxAmplitude();
                        double ratio = maxAmplitude / 150;
                        // 分贝
                        double db = 0;
                        if (ratio > 1) {
                            db = (int) (20 * Math.log10(ratio));
                        }
                        mListener.onAudioVolumeChanged(maxAmplitude / 3000, db);
                    }
                    VoiceRecordManager.this.mHandler.sendEmptyMessageDelayed(2, 150L);
                    break;
                case 3:
                    //暂停
                    stopRec();
                    calculateSumTime();
                    mCurAudioState = pauseState;
                    mCurAudioState.enter();
                    removeMessages();
                    if (mListener != null) {
                        mListener.onPauseRecord();
                    }
                    break;
                case 5:
                    VoiceRecordManager.this.mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VoiceRecordManager.this.stopRec();
                            VoiceRecordManager.this.calculateSumTime();
                            boolean tooShort = checkAudioTimeLength();
                            if (!tooShort) {
                                int duration = (int) (mSumRecTime / 1000);
                                File voiceFile = getOutputVoiceFile(mRecList);
                                VoiceRecordManager.this.deleteTempFiles();
                                mListener.onFinishRecord(duration, voiceFile);
                            } else {
                                ToastUtils.showLong("时间太短");
                                VoiceRecordManager.this.deleteTempFiles();
                            }
                            VoiceRecordManager.this.removeMessages();
                        }
                    }, 500L);
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.idleState;
                    VoiceRecordManager.this.idleState.enter();
                    break;
                case 6:
                    VoiceRecordManager.this.stopRec();
                    VoiceRecordManager.this.removeMessages();
                    VoiceRecordManager.this.deleteTempFiles();
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.idleState;
                    VoiceRecordManager.this.idleState.enter();
                    break;
                default:
                    break;
            }
        }
    }

    class PauseState extends IAudioState {

        @Override
        void enter() {
            super.enter();
            if (VoiceRecordManager.this.mHandler != null) {
                VoiceRecordManager.this.mHandler.removeMessages(2);
            }
        }

        @Override
        void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    VoiceRecordManager.this.mHandler = new Handler(Utils.getApp().getMainLooper(), VoiceRecordManager.this);
                    mListener.onResumeRecord();
                    VoiceRecordManager.this.startRec();
                    VoiceRecordManager.this.mStartRecTime = SystemClock.elapsedRealtime();
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.recordState;
                    VoiceRecordManager.this.sendMessage(2, null);
                    break;
                case 5:
                    VoiceRecordManager.this.mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VoiceRecordManager.this.stopRec();
                            VoiceRecordManager.this.calculateSumTime();
                            boolean tooShort = checkAudioTimeLength();
                            if (!tooShort) {
                                int duration = (int) (mSumRecTime / 1000);
                                File voiceFile = getOutputVoiceFile(mRecList);
                                VoiceRecordManager.this.deleteTempFiles();
                                mListener.onFinishRecord(duration, voiceFile);
                            } else {
                                ToastUtils.showLong("时间太短");
                                VoiceRecordManager.this.deleteTempFiles();
                            }
                            VoiceRecordManager.this.removeMessages();
                        }
                    }, 500L);
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.idleState;
                    VoiceRecordManager.this.idleState.enter();
                    break;
                case 6:
                    VoiceRecordManager.this.stopRec();
                    VoiceRecordManager.this.removeMessages();
                    VoiceRecordManager.this.deleteTempFiles();
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.idleState;
                    VoiceRecordManager.this.idleState.enter();
                    break;
                default:
                    break;
            }
        }
    }

    class IdleState extends IAudioState {

        @Override
        void enter() {
            super.enter();
            if (VoiceRecordManager.this.mHandler != null) {
                VoiceRecordManager.this.mHandler.removeMessages(2);
            }
        }

        @Override
        void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    VoiceRecordManager.this.mHandler = new Handler(Utils.getApp().getMainLooper(), VoiceRecordManager.this);
                    mListener.onStartRecord();
                    VoiceRecordManager.this.startRec();
                    VoiceRecordManager.this.mStartRecTime = SystemClock.elapsedRealtime();
                    VoiceRecordManager.this.mCurAudioState = VoiceRecordManager.this.recordState;
                    VoiceRecordManager.this.sendMessage(2, null);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 合并录音
     *
     * @param list
     * @return
     */
    private File getOutputVoiceFile(ArrayList<File> list) {
        // 创建音频文件,合并的文件放这里
        FileUtils.createOrExistsDir(FILE_PATH);
        File resFile = new File(FILE_PATH, TimeUtils.getNowString() + ".mp3");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(resFile);
        } catch (IOException e) {
        }
        // list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] myByte = new byte[fileInputStream.available()];
                // 文件长度
                int length = myByte.length;
                // 头文件
                if (i == 0) {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 0, length);
                    }
                }
                // 之后的文件，去掉头文件就可以了
                else {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 6, length - 6);
                    }
                }
                fileOutputStream.flush();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 结束后关闭流
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resFile;
    }

    /**
     * 清空暂停录音所产生的几段录音文件
     */
    private void deleteTempFiles() {
        FileUtils.deleteFilesInDir(TEMP_FILE_PATH);
        mRecList.clear();
        mTempFile = null;
    }

    static class SingletonHolder {
        static VoiceRecordManager sInstance = new VoiceRecordManager();
    }

    public void setOnAudioRecordListener(OnAudioRecordListener listener) {
        mListener = listener;
    }

    public interface OnAudioRecordListener {
        /**
         * 开始录音
         */
        void onStartRecord();

        /**
         * 通过这个方法中的音量大小来设置不同的图片（0-10）
         *
         * @param volume 音量
         * @param db     分贝
         */
        void onAudioVolumeChanged(int volume, double db);

        /**
         * 暂停录音
         */
        void onPauseRecord();

        /**
         * 继续录音
         */
        void onResumeRecord();

        /**
         * 录音结束
         *
         * @param duration   秒
         * @param recordFile 录音文件
         */
        void onFinishRecord(int duration, File recordFile);
    }

    public abstract class IAudioState {
        public IAudioState() {
        }

        void enter() {
        }

        /**
         * 处理消息
         *
         * @param msg
         */
        abstract void handleMessage(Message msg);
    }
}
