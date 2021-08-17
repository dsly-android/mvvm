package com.android.dsly.common.bean

/**
 * @author 陈志鹏
 * @date 2021/8/16
 */
data class PageBean<T>(
    /**
     * 是否还有下一页数据
     */
    var hasNext: Boolean,
    /**
     * 当前页最后一条的时间戳
     */
    var lastTimestamp: Long,
    /**
     * 返回查询的结果
     */
    var results: List<T>,
    /**
     * 当前页数
     */
    var pageNum: Int
)