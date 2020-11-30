package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityRandomAccessFileBinding;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileActivity extends BaseActivity<ActivityRandomAccessFileBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_random_access_file;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        file = new File(getExternalCacheDir().getAbsolutePath(), "aaa.txt");
    }

    private File file = null;

    public void testRandomAccessFileWriter() throws IOException {
        if (file.exists()) {
            file.delete();
        }

        RandomAccessFile rsfWriter = new RandomAccessFile(file, "rw");

        rsfWriter.seek(10000);
        printFileLength(rsfWriter);        //result: 0

        rsfWriter.setLength(10000);
        System.out.println("oo");
        printFileLength(rsfWriter);        //result: 0
        System.out.println("xx");

        //一个文字占三个字节
        rsfWriter.writeUTF("啦啦啦");
        printFileLength(rsfWriter);

        //一个字符占两个字节
        rsfWriter.writeChar('a');
        rsfWriter.writeChars("abcde");
        printFileLength(rsfWriter);

        //从文件中间写,文件的内容会被覆盖
        rsfWriter.seek(5000);
        char[] cbuf = new char[100];
        for (int i = 0; i < cbuf.length; i++) {
            cbuf[i] = 'a';
            rsfWriter.writeChar(cbuf[i]);
        }
        printFileLength(rsfWriter);

        byte[] bbuf = new byte[100];
        for (int i = 0; i < bbuf.length; i++) {
            bbuf[i] = 1;
        }
        rsfWriter.seek(1000);
        rsfWriter.writeBytes(new String(bbuf));
        printFileLength(rsfWriter);
    }

    public void testRandomAccessFileRead() throws IOException {
        RandomAccessFile rsfReader = new RandomAccessFile(file, "r");

        rsfReader.seek(10000);
        System.out.println(rsfReader.readUTF());

        rsfReader.seek(5000);
        byte[] bbuf = new byte[200];
        rsfReader.read(bbuf);
        System.out.println(new String(bbuf));

        byte[] bbuf2 = new byte[100];
        rsfReader.seek(1000);
        rsfReader.read(bbuf2, 0, 100);
        for (byte b : bbuf2) {
            System.out.print(b);
        }
        System.out.println();

        byte[] bbuf3 = new byte[12];
        rsfReader.seek(10014);
        rsfReader.read(bbuf3);
        System.out.println(new String(bbuf3));
    }

    private void printFileLength(RandomAccessFile rsfWriter) throws IOException {
        LogUtils.i("file length: " + rsfWriter.length() + "  file pointer: " + rsfWriter.getFilePointer());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write:
                try {
                    testRandomAccessFileWriter();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_read:
                try {
                    testRandomAccessFileRead();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
