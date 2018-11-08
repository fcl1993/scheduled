package com.dafy.scheduled.core;

import com.dafy.scheduled.core.service.JobService;
import com.dafy.scheduled.core.data.Job;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 接受外部任务
 *
 * @author 付成垒
 */
@ReferencePlugin
public class ServiceInstance
{

    @Linked
    private static JobService httpJobService;

    /*@Linked
    private static HttpTaskServer httpTaskServer;

    @Linked
    private static HttpHandOutPatternService httpHandOutPatternService;*/

    /**
     * 接受Job
     */
    public static boolean submitLocalJob(Integer time, String name, String url, String type, Integer overTime, String args)
    {
        Date date = new Date();
        Job httpJobMataData = new Job();
        httpJobMataData.setnState(0);
        httpJobMataData.setStrName(name);
        httpJobMataData.setStrMessage("未执行");
        httpJobMataData.setStrUrl(url);
        httpJobMataData.setDtCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        httpJobMataData.setStrArgs(args);
        httpJobMataData.setnOverTime(overTime == null ? 5000 : overTime);
        if (type == null || "".equals(type.trim()))
        {
            httpJobMataData.setStrType("POST");
        }
        else
        {
            httpJobMataData.setStrType(type);
        }
        if (time != null)
        {
            httpJobMataData.setnTime(time);
            httpJobMataData.setDtExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time + date.getTime())));
        }
        else
        {
            httpJobMataData.setnTime(0);
            httpJobMataData.setDtExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }
        if (!httpJobService.insertData(httpJobMataData))
        {
            return false;
        }
        return true;
    }

    /**
     * 接受Task
     */
    /*public static boolean submitLocalTask(String executeTime, String name, String url, String type, Integer executeType, Integer executeUnit, Integer overTime, String args)
    {
        Date date = new Date();
        HttpTaskData httpTaskMataData = new HttpTaskData();
        httpTaskMataData.setState(0);
        httpTaskMataData.setName(name);
        httpTaskMataData.setMessage("未执行");
        httpTaskMataData.setUrl(url);
        httpTaskMataData.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        httpTaskMataData.setArgs(args);
        httpTaskMataData.setOverTime(overTime);
        if (type == null || "".equals(type.trim()))
        {
            httpTaskMataData.setType("POST");
        }
        else
        {
            httpTaskMataData.setType(type);
        }
        if (executeTime != null && !executeTime.equals(""))
        {
            httpTaskMataData.setExecuteTime(executeTime);
        }
        else
        {
            httpTaskMataData.setExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }
        if (overTime != null)
            httpTaskMataData.setOverTime(overTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        httpTaskMataData.setExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        httpTaskMataData.setExecuteUnit(executeUnit);
        if (!httpTaskServer.insertData(httpTaskMataData))
        {
            return false;
        }
        return true;
    }

    *//**
 * 接受HandOutTask
 *//*
    public static boolean submitHandOutTask(Integer concurrencyNumber, String name, Integer lazyTime, Integer parameterType, String sourceURL, String sourceType, String destinationURL, String destinationType, Integer executeType, Integer executeUnit, Integer overTime)
    {
        Date date = new Date();
        HttpHandOutPatternData httpHandOutPatternMataData = new HttpHandOutPatternData();
        httpHandOutPatternMataData.setName(name);
        httpHandOutPatternMataData.setState(0);
        httpHandOutPatternMataData.setMessage("未执行");
        httpHandOutPatternMataData.setSourceURL(sourceURL);
        if (concurrencyNumber == null)
            httpHandOutPatternMataData.setConcurrencyNumber(1);
        else
            httpHandOutPatternMataData.setConcurrencyNumber(concurrencyNumber);
        if (parameterType == null)
            httpHandOutPatternMataData.setParameterType(1);
        else
            httpHandOutPatternMataData.setParameterType(parameterType);
        if (sourceType == null || "".equals(sourceType))
            httpHandOutPatternMataData.setSourceType("POST");
        else
            httpHandOutPatternMataData.setSourceType(sourceType);
        httpHandOutPatternMataData.setDestinationURL(destinationURL);
        if (destinationType == null || "".equals(destinationType))
            httpHandOutPatternMataData.setDestinationType("POST");
        else
            httpHandOutPatternMataData.setDestinationType(destinationType);
        httpHandOutPatternMataData.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        if (overTime == null)
            httpHandOutPatternMataData.setOverTime(0);
        else
            httpHandOutPatternMataData.setOverTime(overTime);
        httpHandOutPatternMataData.setExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        if (lazyTime != null)
        {
            httpHandOutPatternMataData.setLazyTime(lazyTime);
            httpHandOutPatternMataData.setExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lazyTime + date.getTime())));
        }
        else
        {
            httpHandOutPatternMataData.setLazyTime(0);
            httpHandOutPatternMataData.setExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        }
        if (overTime != null)
            httpHandOutPatternMataData.setOverTime(overTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        httpHandOutPatternMataData.setExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        httpHandOutPatternMataData.setExecuteUnit(executeUnit);
        if (!httpHandOutPatternService.insertHttpHandOutPattern(httpHandOutPatternMataData))
        {
            return false;
        }
        return true;
    }*/

}
