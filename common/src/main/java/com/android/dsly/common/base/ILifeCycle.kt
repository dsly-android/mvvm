package com.android.dsly.common.base

import android.os.Bundle

/**
 * @author 陈志鹏
 * @date 2021/8/17
 */
interface ILifeCycle {
    /**
     * 获取布局id
     *
     * @return
     */
    fun getLayoutId(): Int

    /**
     * 初始化 view
     *
     * @param savedInstanceState
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化事件
     */
    fun initEvent()

    /**
     * 初始化数据
     */
    fun initData()
}