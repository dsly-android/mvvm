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


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.blankj.utilcode.util.ConvertUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 实时录音工具类
 */
public class AudioProducer {

    //最小缓冲区大小
    private int mMinBufferSize;

    private boolean running;
    private AudioRecord recorder;
    private ByteBuffer chunck;

    private OnDataListener mListener;

    public static AudioProducer getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        public static AudioProducer INSTANCE = new AudioProducer();
    }

    public void setOnDataListener(OnDataListener listener) {
        mListener = listener;
    }

    private synchronized boolean prepare() {
        //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法可以获得。
        mMinBufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);//计算最小缓冲区

        int bufferSizeInBytes = mMinBufferSize * 4;
        this.chunck = ByteBuffer.allocateDirect(bufferSizeInBytes);

        //MediaRecorder.AudioSource.VOICE_COMMUNICATION   消除回音
        this.recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,
                8000, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);
        if (this.recorder.getState() == AudioRecord.STATE_INITIALIZED) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean start() {
        boolean prepare = prepare();
        if (!prepare) {
            return false;
        }
        if (running) {
            return false;
        }
        if (this.recorder != null) {
            this.running = true;
            new Thread(this.runnableRecorder).start();
            return true;
        }
        return false;
    }

    public synchronized boolean stop() {
        if (this.recorder != null) {
            this.running = false;
            AudioProducerData.getInstance().clear();
            mListener = null;
            return true;
        }
        return false;
    }

    private long startTime;

    private Runnable runnableRecorder = new Runnable() {
        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

            recorder.startRecording();

            int read;

            startTime = System.currentTimeMillis();

            while (running) {
                if (recorder == null) {
                    break;
                }
                if ((read = recorder.read(chunck, mMinBufferSize)) > 0) {
                    byte[] bytes = Arrays.copyOf(chunck.array(), read);

                    AudioProducerData.getInstance().put(bytes);
                    long timeDiff = System.currentTimeMillis() - startTime;
                    if (timeDiff > 120) {
                        startTime = System.currentTimeMillis();

                        byte[] voiceBytes = AudioProducerData.getInstance().toByteArray();
                        //录音数据上传到服务器
                        try {
                            if (mListener != null) {
//                                byte[] compress = GzipUtils.compress(voiceBytes);
                                String voiceStr = ConvertUtils.bytes2HexString(voiceBytes);
                                mListener.onVoiceData(voiceStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        AudioProducerData.getInstance().clear();
                    }
                }
            }

            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;

                System.gc();
            }

        }
    };

    public interface OnDataListener {
        void onVoiceData(String data);
    }
}
