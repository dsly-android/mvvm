package com.htxtdshopping.htxtd.frame.utils;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;

import com.android.dsly.common.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * 音乐播放管理
 *
 * @author 陈志鹏
 * @date 2018/11/15
 */
public class VoicePlayManager {

    public final int MEDIA_STATE_UNKNOWN = 200;
    public final int MEDIA_STATE_PLAY_DOING = 320;
    public final int MEDIA_STATE_PLAY_STOP = 310;
    public final int MEDIA_STATE_PLAY_PAUSE = 330;
    public final int MSG_TIME_INTERVAL = 100;

    private MediaPlayer mMediaPlayer;
    private int mDeviceState = MEDIA_STATE_UNKNOWN;
    private SeekBar mSBPlayProgress;
    /**
     * 播放音频文件位置
     */
    private String mPlayFilePath;
    /**
     * 播放监听
     */
    private VoicePlayCallBack mCallBack;
    /**
     * 是否循环播放
     */
    private boolean mIsLooping;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIME_INTERVAL:
                    if (mDeviceState == MEDIA_STATE_PLAY_DOING) {
                        int current = mMediaPlayer.getCurrentPosition();
                        if (mSBPlayProgress != null) {
                            mSBPlayProgress.setProgress(current);
                        }
                        //回调播放进度
                        if (mCallBack != null) {
                            mCallBack.playing(current);
                        }
                        mHandler.sendEmptyMessageDelayed(MSG_TIME_INTERVAL, 1000);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static VoicePlayManager getInstance() {
        return VoicePlayManager.SingletonHolder.sInstance;
    }

    static class SingletonHolder {
        static VoicePlayManager sInstance = new VoicePlayManager();
    }

    /**
     * 播放器结束监听
     */
    private MediaPlayer.OnCompletionListener mPlayCompetedListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mDeviceState = MEDIA_STATE_PLAY_STOP;
            mHandler.removeMessages(MSG_TIME_INTERVAL);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mSBPlayProgress != null) {
                mSBPlayProgress.setProgress(0);
            }
            if (mCallBack != null) {
                mCallBack.finish(mPlayFilePath);
            }
        }
    };

    /**
     * 播放SeekBar监听
     *
     * @param seekBar
     */
    public void setSeekBarListener(SeekBar seekBar) {
        mSBPlayProgress = seekBar;
        if (mSBPlayProgress != null) {
            mSBPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                /**
                 * 用户已启动触摸手势
                 * @param seekBar
                 */
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mHandler.removeMessages(MSG_TIME_INTERVAL);
                    if (mDeviceState == MEDIA_STATE_PLAY_DOING) {
                        pauseMedia(mMediaPlayer);
                    }
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mHandler.removeMessages(MSG_TIME_INTERVAL);
                    //播放进度
                    if (mCallBack != null) {
                        mCallBack.playing(progress);
                    }
                }

                /**
                 * 用户已完成触摸手势
                 * @param seekBar
                 */
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekToMedia(mMediaPlayer, mSBPlayProgress.getProgress());
                    if (mDeviceState == MEDIA_STATE_PLAY_DOING) {
                        playMedia(mMediaPlayer);
                        mHandler.sendEmptyMessage(MSG_TIME_INTERVAL);
                    }
                }
            });
        }
    }

    /**
     * 开始播放（外部调）
     *
     * @param filePath 音频存放文件夹
     */
    public void startPlay(String filePath) {
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            if (mCallBack != null) {
                mCallBack.finish(filePath);
            }
            ToastUtils.showShort("文件不存在");
        } else {
            //停止
            if (isPlaying() && mCallBack != null && mPlayFilePath != null){
                mCallBack.stop(mPlayFilePath);
            }
            mPlayFilePath = filePath;
            startPlay(true);
        }
    }

    /**
     * 开始播放（内部调）
     *
     * @param init
     */
    private void startPlay(boolean init) {
        try {
            VoiceRecordManager.getInstance().stop();

            stopMedia(mMediaPlayer);
            mMediaPlayer = null;

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setLooping(mIsLooping);
            mMediaPlayer.setOnCompletionListener(mPlayCompetedListener);

            if (prepareMedia(mMediaPlayer, mPlayFilePath)) {
                if (mSBPlayProgress != null) {
                    mSBPlayProgress.setMax(Math.max(1, mMediaPlayer.getDuration()));
                }
                if (init) {
                    if (mSBPlayProgress != null) {
                        mSBPlayProgress.setProgress(0);
                    }
                    seekToMedia(mMediaPlayer, 0);
                } else {
                    seekToMedia(mMediaPlayer, mSBPlayProgress.getProgress());
                }
                //播放进度回调
                if (mCallBack != null) {
                    mCallBack.voiceTotalLength(mMediaPlayer.getDuration());
                    mCallBack.playing(0);
                }
                mDeviceState = MEDIA_STATE_PLAY_DOING;
                if (playMedia(mMediaPlayer)) {
                    mHandler.removeMessages(MSG_TIME_INTERVAL);
                    mHandler.sendEmptyMessage(MSG_TIME_INTERVAL);
                }
            }
        } catch (Exception e) {
            Log.e("播放出错了", e.getMessage());
        }
    }

    /**
     * 继续播放
     */
    public void continuePlay(){
        if (mDeviceState == MEDIA_STATE_PLAY_PAUSE) {
            mDeviceState = MEDIA_STATE_PLAY_DOING;
            playMedia(mMediaPlayer);
            //播放中
            mHandler.removeMessages(MSG_TIME_INTERVAL);
            mHandler.sendEmptyMessage(MSG_TIME_INTERVAL);
        } else if (mDeviceState == MEDIA_STATE_PLAY_STOP) {
            //播放
            if (!TextUtils.isEmpty(mPlayFilePath)) {
                startPlay(false);
            }
        }
    }

    /**
     * 暂停播放
     */
    public void pausePlay(){
        if (mDeviceState == MEDIA_STATE_PLAY_DOING) {
            mDeviceState = MEDIA_STATE_PLAY_PAUSE;
            pauseMedia(mMediaPlayer);
            //暂停
            if (mCallBack != null) {
                mCallBack.pause(mPlayFilePath);
            }
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        mHandler.removeMessages(MSG_TIME_INTERVAL);
        mDeviceState = MEDIA_STATE_PLAY_STOP;
        stopMedia(mMediaPlayer);
        mMediaPlayer = null;
        //停止
        if (mCallBack != null){
            mCallBack.stop(mPlayFilePath);
        }
    }

    /**
     * 播放录音准备工作
     *
     * @param mp
     * @param file
     * @return
     */
    private boolean prepareMedia(MediaPlayer mp, String file) {
        boolean result = false;
        try {
            mp.setDataSource(new FileInputStream(file).getFD());
            mp.prepare();
            result = true;
        } catch (Exception e) {
        }

        return result;
    }

    /**
     * 播放录音开始
     *
     * @param mp
     * @return
     */
    private boolean playMedia(MediaPlayer mp) {
        boolean result = false;
        try {
            if (mp != null) {
                mp.start();
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 拖动播放进度条
     *
     * @param mp
     * @param timestamp
     * @return
     */
    private boolean seekToMedia(MediaPlayer mp, int timestamp) {
        boolean result = false;
        try {
            if (mp != null && timestamp >= 0) {
                mp.seekTo(timestamp);
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 停止播放
     *
     * @param mp
     * @return
     */
    private boolean stopMedia(MediaPlayer mp) {
        boolean result = false;
        try {
            if (mp != null) {
                mp.stop();
                mp.release();
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 暂停播放
     *
     * @param mp
     */
    private boolean pauseMedia(MediaPlayer mp) {
        boolean result = false;
        try {
            if (mp != null) {
                mp.pause();
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public VoicePlayManager isLooping(boolean isLooping) {
        mIsLooping = isLooping;
        return this;
    }

    /**
     * 是否在播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return mDeviceState == MEDIA_STATE_PLAY_DOING;
    }

    public void setVoicePlayListener(VoicePlayCallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 播放录音回调监听
     */
    public interface VoicePlayCallBack {

        /**
         * 音频长度
         *
         * @param totalLength 音频长度（单位：毫秒）
         */
        void voiceTotalLength(long totalLength);

        /**
         * 播放中
         *
         * @param playTimeStamp 开始播放的时间戳，以豪秒为单位
         */
        void playing(long playTimeStamp);

        /**
         * 播放暂停
         */
        void pause(String playFilePath);

        /**
         * 播放开始
         */
        void start(String playFilePath);

        /**
         * 播放停止
         */
        void stop(String playFilePath);

        /**
         * 播放结束
         */
        void finish(String playFilePath);
    }
}
