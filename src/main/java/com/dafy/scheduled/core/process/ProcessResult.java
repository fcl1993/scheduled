package com.dafy.scheduled.core.process;

/**
 * 调度结果
 *
 * @author 付成垒
 */
public class ProcessResult
{

    private String message;

    private String time;

    //true为默认执行 false为立即执行
    private boolean type;

    public ProcessResult()
    {
    }

    public ProcessResult(String message, String time)
    {
        this.message = message;
        this.time = time;
    }

    public ProcessResult(String message, String time, boolean type)
    {
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public boolean isType()
    {
        return type;
    }

    public void setType(boolean type)
    {
        this.type = type;
    }

}
