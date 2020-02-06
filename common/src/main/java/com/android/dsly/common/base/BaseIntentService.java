package com.android.dsly.common.base;

import android.app.IntentService;

/**
 * @author 陈志鹏
 * @date 2019-12-07
 */
public abstract class BaseIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BaseIntentService(String name) {
        super(name);
    }
}
