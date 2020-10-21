package com.htxtdshopping.htxtd.frame.utils;

import java.util.Vector;

/**
 * @author 陈志鹏
 * @date 2020/3/30
 */
public class AudioConsumerData {

    private Vector<byte[]> vector = new Vector();

    public static AudioConsumerData getInstance() {
        return Holder.INSTANCE;
    }

    public static class Holder {
        public static AudioConsumerData INSTANCE = new AudioConsumerData();
    }

    public void put(byte[] voice) {
        if (vector.size() >= 5) {
            remove(0);
        }
        vector.add(voice);
    }

    public void remove(int pos) {
        vector.remove(pos);
    }

    public byte[] getFirst() {
        byte[] bytes = vector.get(0);
        remove(0);
        return bytes;
    }

    public int size() {
        return vector.size();
    }

    public boolean hasDatas() {
        return vector.size() != 0;
    }

    public void clear(){
        vector.clear();
    }
}
