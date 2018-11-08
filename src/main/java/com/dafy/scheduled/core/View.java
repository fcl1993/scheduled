package com.dafy.scheduled.core;

import com.dafy.scheduled.core.util.Page;

import java.io.Serializable;

/**
 * 查询视图
 *
 * @author 付成垒
 */
public class View<T> implements Serializable
{

    private static final long serialVersionUID = 7127680407063547813L;

    private long count;

    private T data;

    private Integer state;

    public View(T data)
    {
        this.state = 0;
        this.count = 0;
        this.data = data;
    }

    public View(long count, int pageSize, T data)
    {
        this.count = Page.getTotal(count, pageSize);
        this.data = data;
    }

    public View(T data, Integer state)
    {
        this.data = data;
        this.state = state;
    }

    public View(Integer state)
    {
        this.state = state;
    }

    public long getCount()
    {
        return count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

}
