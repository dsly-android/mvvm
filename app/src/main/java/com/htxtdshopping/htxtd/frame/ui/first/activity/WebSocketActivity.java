package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.EventBusTag;
import com.blankj.utilcode.util.ServiceUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityWebSocketBinding;
import com.htxtdshopping.htxtd.frame.event.SocketSendEvent;
import com.htxtdshopping.htxtd.frame.service.WebSocketService;
import com.jeremyliao.liveeventbus.LiveEventBus;

public class WebSocketActivity extends BaseFitsWindowActivity<ActivityWebSocketBinding, BaseViewModel> implements View.OnClickListener {

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
        mBinding.btnClick.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceUtils.stopService(WebSocketService.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                SocketSendEvent event = new SocketSendEvent();
                event.setMsg(mBinding.etInput.getText().toString());
                LiveEventBus.get(EventBusTag.EVENT_SEND_MESSAGE,SocketSendEvent.class).post(event);
                break;
            default:
                break;
        }
    }
}