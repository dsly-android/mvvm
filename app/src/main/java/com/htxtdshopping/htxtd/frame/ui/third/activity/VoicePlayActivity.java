package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.RecordAdapter;
import com.htxtdshopping.htxtd.frame.utils.VoicePlayManager;
import com.htxtdshopping.htxtd.frame.utils.VoiceRecordManager;

import java.io.File;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @author chenzhipeng
 */
public class VoicePlayActivity extends BaseActivity {

    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    private RecordAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_play;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecordAdapter();
        mRvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VoicePlayManager.getInstance().startPlay(mAdapter.getData().get(position).getAbsolutePath());
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
        FileUtils.deleteDir(VoiceRecordManager.FILE_PATH);
    }
}