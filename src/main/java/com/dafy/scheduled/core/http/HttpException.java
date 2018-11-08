package com.dafy.scheduled.core.http;

/**
 * http异常
 *
 * @author 付成垒
 */
public class HttpException extends RuntimeException
{

    private Integer code;

    public HttpException(Integer code, String message)
    {
        super(message);
        this.code = code;
    }

    public Integer getCode()
    {
        return code;
    }
}
