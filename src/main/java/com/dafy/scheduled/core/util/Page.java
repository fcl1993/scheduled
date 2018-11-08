package com.dafy.scheduled.core.util;

/**
 * 分页计算
 *
 * @author 付成垒
 */
public class Page
{

    public static long getTotal(long count , int pageSize)
    {
        return count != 0 && count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
    }

    public static long getLimit(int currentPage , int pageSize)
    {
        return (currentPage - 1) * pageSize;
    }

}
