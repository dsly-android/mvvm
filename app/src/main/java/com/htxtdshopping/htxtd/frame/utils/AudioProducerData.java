package com.htxtdshopping.htxtd.frame.utils;

import java.util.Vector;

/**
 * @author 陈志鹏
 * @date 2020/3/31
 */
public class AudioProducerData {

    private Vector<Byte> voices = new Vector();

    public static AudioProducerData getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        public static AudioProducerData INSTANCE = new AudioProducerData();
    }

    public void put(byte[] voice) {
        for (int i = 0; i < voice.length; i++) {
            voices.add(voice[i]);
        }
    }

    public int size() {
        return voices.size();
    }

    public boolean hasDatas() {
        return voices.size() != 0;
    }

    public Vector<Byte> getData() {
        return voices;
    }

    public byte[] toByteArray(){
        byte[] datas = new byte[voices.size()];
        for (int i = 0; i <datas.length; i++) {
            datas[i] = voices.get(i);
        }
        return datas;
    }

    public void clear(){
        voices.clear();
    }
}