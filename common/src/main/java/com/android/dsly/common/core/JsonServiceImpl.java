package com.android.dsly.common.core;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

/**
 * @author 陈志鹏
 * @date 2019-12-15
 */
@Route(path = "/common/json")
public class JsonServiceImpl implements SerializationService {

    @Override
    public <T> T json2Object(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return JSON.toJSONString(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return JSON.parseObject(input, clazz);
    }

    @Override
    public void init(Context context) {

    }
}
