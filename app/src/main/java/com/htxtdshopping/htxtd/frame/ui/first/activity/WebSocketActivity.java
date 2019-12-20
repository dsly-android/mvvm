package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.blankj.utilcode.util.ServiceUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.event.SocketSendEvent;
import com.htxtdshopping.htxtd.frame.service.WebSocketService;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class WebSocketActivity extends BaseFitsWindowActivity {

    @BindView(R.id.et_input)
    EditText mEtInput;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_socket;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ServiceUtils.startService(WebSocketService.class);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                SocketSendEvent event = new SocketSendEvent();
                event.setMsg(mEtInput.getText().toString());
                EventBus.getDefault().post(event);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceUtils.stopService(WebSocketService.class);
    }
}