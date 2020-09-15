package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityVoicePlayBinding;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.RecordAdapter;
import com.htxtdshopping.htxtd.frame.utils.VoicePlayManager;
import com.htxtdshopping.htxtd.frame.utils.VoiceRecordManager;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * @author chenzhipeng
 */
public class VoicePlayActivity extends BaseActivity<ActivityVoicePlayBinding, BaseViewModel> {

    private RecordAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecordAdapter();
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoicePlayManager.getInstance().startPlay(mAdapter.getData().get(position).getAbsolutePath());
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_icon:
                        VoicePlayManager instance = VoicePlayManager.getInstance();
                        if (instance.isPlaying()) {
                            instance.pausePlay();
                        } else {
                            instance.continuePlay();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        List<File> files = FileUtils.listFilesInDir(VoiceRecordManager.FILE_PATH);
        mAdapter.setNewData(files);
    }

    public void deleteDir(View view) {
        FileUtils.deleteAllInDir(VoiceRecordManager.FILE_PATH);
    }
}