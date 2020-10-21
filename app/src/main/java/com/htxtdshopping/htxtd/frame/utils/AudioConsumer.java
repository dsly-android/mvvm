/*
 * Copyright (C) 2010 Mamadou Diop.
 *
 * Contact: Mamadou Diop <diopmamadou(at)doubango.org>
 *
 * This file is part of imsdroid Project (http://code.google.com/p/imsdroid)
 *
 * imsdroid is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * imsdroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package com.htxtdshopping.htxtd.frame.utils;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.blankj.utilcode.util.Utils;

/**
 * 实时播放工具类
 */
public class AudioConsumer {

    //是否正在运行
    private boolean running;

    private AudioTrack track;

    private AudioManager mManager;

    public static AudioConsumer getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        public static AudioConsumer INSTANCE = new AudioConsumer();
    }

    public AudioConsumer() {
        mManager = (AudioManager) Utils.getApp().getSystemService(Context.AUDIO_SERVICE);
        mManager.setMode(AudioManager.MODE_IN_CALL);
    }

    private synchronized boolean prepare() {
        setSpeakerphoneOn(false);

        this.track = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                1 * 1024, AudioTrack.MODE_STREAM);
        if (this.track.getState() == AudioTrack.STATE_INITIALIZED) {
            this.track.setStereoVolume(1, 1);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean start() {
        boolean prepare = prepare();
        if (!prepare){
            return prepare;
        }
        if (running) {
            return false;
        }
        if (this.track != null) {
            this.running = true;
            new Thread(this.runnablePlayer).start();
            return true;
        }
        return false;
    }

    public synchronized boolean pause() {
        if (this.track != null) {
            this.track.pause();
            return true;
        }
        return false;
    }

    public synchronized boolean stop() {
        if (this.track != null) {
            this.running = false;
            AudioConsumerData.getInstance().clear();
            return true;
        }
        return false;
    }

    private int frameCount = 0;

    private Runnable runnablePlayer = new Runnable() {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            while (running) {
                if (track == null) {
                    break;
                }
                //消除电流声
                frameCount++;
                if (frameCount % 5 == 0) {
                    track.flush();
                    System.gc();
                }

                if (AudioConsumerData.getInstance().hasDatas()) {
                    byte[] voiceBytes = AudioConsumerData.getInstance().getFirst();
                    track.write(voiceBytes, 0, voiceBytes.length);

                    track.play();
                }
            }

            if (track != null) {
                track.stop();
                track.release();
                track = null;

                System.gc();
            }
        }
    };

    /**
     * 是否免提
     *
     * @param on
     */
    public void setSpeakerphoneOn(boolean on) {
        mManager.setSpeakerphoneOn(on);
    }

    public boolean isSpeakerphoneOn() {
        return mManager.isSpeakerphoneOn();
    }
}
