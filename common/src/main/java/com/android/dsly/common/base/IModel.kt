package com.android.dsly.common.base

/**
 * @author 陈志鹏
 * @date 2021/8/17
 */
interface IModel {
    /**
     * ViewModel销毁时清除Model，与ViewModel共消亡。Model层同样不能持有长生命周期对象
     */
    fun onCleared()
}