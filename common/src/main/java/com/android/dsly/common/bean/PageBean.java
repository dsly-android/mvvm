package com.android.dsly.common.bean;

import java.util.List;

/**
 * @author 陈志鹏
 * @date 2021/8/17
 */
public class PageBean<T> {
    /**
     * 是否还有下一页数据
     */
    private boolean hasNext;
    /**
     * 当前页最后一条的时间戳
     */
    private Long lastTimestamp;
    /**
     * 返回查询的结果
     */
    private List<T> results;
    /**
     * 当前页数
     */
    private int pageNum;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
