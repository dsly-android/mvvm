package com.android.dsly.web.activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;

import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.web.R;
import com.android.dsly.web.base.BaseWebActivity;
import com.android.dsly.web.databinding.WebActivityJsWebBinding;
import com.android.dsly.web.web.AndroidInterface;

import org.json.JSONObject;

public class JsWebActivity extends BaseWebActivity<WebActivityJsWebBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.web_activity_js_web;
    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        //注入对象
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(mAgentWeb,this));
    }

    @Override
    protected String getUrl() {
        return "file:///android_asset/js_interaction/hello.html";
    }

    @Override
    protected ViewGroup getAgentWebParent() {
        return mBinding.llParent;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.callJsNoParamsButton:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroid");
                break;
            case R.id.callJsOneParamsButton:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidParam","Hello ! Agentweb");
                break;
            case R.id.callJsMoreParamsButton:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidMoreParams", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.i("Info","value:"+value);
                    }
                },getJson(),"say:", " Hello! Agentweb");
                break;
            case R.id.jsJavaCommunicationButton:
                mAgentWeb.getJsAccessEntrace().quickCallJs("callByAndroidInteraction","你好Js");
                break;
        }
    }

    private String getJson(){

        String result="";
        try {
            JSONObject mJSONObject=new JSONObject();
            mJSONObject.put("id",1);
            mJSONObject.put("name","Agentweb");
            mJSONObject.put("age",18);
            result= mJSONObject.toString();
        }catch (Exception e){

        }

        return result;
    }
}