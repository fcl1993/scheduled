package com.dafy.scheduled.core.http;

import java.util.HashMap;
import java.util.List;

/**
 * task结果
 *
 * @author 付成垒
 */
public class HttpReturn<T>
{

    private Integer code;

    private String message;

    private List<T> data;

    static HashMap<Integer, String> entity;

    public HttpReturn(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public HttpReturn(Integer code, String message, List<T> data)
    {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }

    static
    {
        if (entity == null)
        {
            entity = new HashMap<Integer, String>();
            synchronized (entity)
            {
                entity.put(0, "任务成功");
                entity.put(1, "业务端处理业务时返回了失败");
                entity.put(2, "返回结果非期望值失败,无法确认业务端是否处理成功(原因可能是接口名写错了)");
                entity.put(3, "重试次数超过三次仍就无法访问接口失败失败,可能原因是访问目标主机速度过慢,请检查网络环境");
                entity.put(4, "找不到访问的主机,请检查主机域名是否错误");
                entity.put(5, "没有检测到业务端返回,业务端可能调用过程中已宕机");
                entity.put(6, "任务处理超时,已强行结束调用(并不代表任务失败,任务可能已成功)");
                entity.put(7, "连接超时,访问目标主机速度过慢,请检查网络环境");
                entity.put(8, "访问主机接口超时,请检查目标服务是否启动");
                entity.put(9, "调度服务器socket资源被消耗殆尽");
                entity.put(10, "其它未知的异常");
                //暂时不支持ssl
                //entity.put(11, "SSL握手超时");
                //entity.put(12, "SSL异常");
            }
        }
    }

}
