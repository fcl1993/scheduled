package com.dafy.scheduled.web.service;

import com.dafy.scheduled.core.ServiceInstance;
import org.springframework.stereotype.Service;

/**
 * 接受任务
 *
 * @author 付成垒
 */
@Service
public class HttpServiceInstance
{

    /**
     * 增加httpJob
     */
    public boolean createHttpJob(Integer time, String name, String url, String type, Integer overTime, String args)
    {
        return ServiceInstance.submitLocalJob(time, name, url, type, overTime, args);
    }

    /**
     * 增加httpTask
     */
    /*public boolean createHttpTask(String executeTime, String name, String url, String type, Integer executeType, Integer executeUnit, Integer overTime, String args)
    {
        return ServiceInstance.submitLocalTask(executeTime, name, url, type, executeType, executeUnit, overTime, args);
    }*/

    /**
     * 增加HttpHandOutPattern
     */
    /*public boolean createHandOutTask(Integer concurrencyNumber, String name, Integer lazyTime, Integer parameterType, String sourceURL, String sourceType, String destinationURL, String destinationType, Integer executeType, Integer executeUnit, Integer overTime)
    {
        return ServiceInstance.submitHandOutTask(concurrencyNumber, name, lazyTime, parameterType, sourceURL, sourceType, destinationURL, destinationType, executeType, executeUnit, overTime);
    }*/

}
