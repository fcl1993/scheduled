package com.dafy.scheduled.web.controller;

import com.dafy.scheduled.core.View;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.service.HandOutService;
import com.dafy.scheduled.core.service.JobService;
import com.dafy.scheduled.core.service.TaskService;
import com.dafy.scheduled.web.service.HttpServiceInstance;
import com.dafy.scheduled.web.util.SecurityCheck;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接受任务
 *
 * @author 付成垒
 */
@RestController
@RequestMapping(value = "task")
public class HttpServiceController
{

    private static Logger logger = Logger.getLogger(HttpServiceController.class);

    @Autowired
    private HttpServiceInstance httpServiceInstance;

    @Autowired
    private SecurityCheck securityCheck;

    @Autowired
    private JobService httpJobService;

    @Autowired
    private TaskService httpTaskServer;

    @Autowired
    private HandOutService httpHandOutPatternService;

    /**
     * 接受httpJob
     */
    @RequestMapping(value = "createHttpJob", produces = "application/json;charset=utf-8")
    public Boolean createHttpJob(
            @RequestParam(value = "time", required = true) Integer time,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "url", required = true) String url,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "overTime", required = false) Integer overTime,
            @RequestParam(value = "args", required = false) String args,
            @RequestParam(value = "innerSecurity", required = true) String innerSecurity
    )
    {
        if (!securityCheck.innerSecurityCheck(innerSecurity))
        {
            logger.error("安全校验失败,安全码校验未通过");
            return null;
        }
        return httpServiceInstance.createHttpJob(time, name, url, type, overTime, args);
    }

    /**
     * 更具taskId立即执行Task
     */
    @RequestMapping(value = "jobImmediateExecution", produces = "application/json;charset=utf-8")
    public View<Boolean> jobImmediateExecution(
            @RequestParam(value = "type", required = true) Integer type,
            @RequestParam(value = "taskId", required = true) Long taskId,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        return new View<Boolean>(httpJobService.immediateExecution(taskId , type));
    }

    /**
     * 接受httpTask
     */
    @RequestMapping(value = "createOrUpdateHttpTask", produces = "application/json;charset=utf-8")
    public View<Boolean> createHttpTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "url", required = true) String url,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "executeTime", required = true) String executeTime,
            @RequestParam(value = "executeType", required = true) Integer executeType,
            @RequestParam(value = "executeUnit", required = true) Integer executeUnit,
            @RequestParam(value = "overTime", required = false) Integer overTime,
            @RequestParam(value = "args", required = false) String args,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(httpTaskServer.createHttpTask(executeTime, name, url, type, executeType, executeUnit, overTime, args));
        else
            return new View<Boolean>(httpTaskServer.updateHttpTask(id, executeTime, name, url, type, executeType, executeUnit, overTime, args));
    }

    /**
     * 开启httpTask
     */
    @RequestMapping(value = "startHttpTask", produces = "application/json;charset=utf-8")
    public View<Boolean> startHttpTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpTaskServer.startTask(id));
    }

    /**
     * 关闭httpTask
     */
    @RequestMapping(value = "closeHttpTask", produces = "application/json;charset=utf-8")
    public View<Boolean> closeHttpTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpTaskServer.closeTask(id));
    }

    /**
     * 删除httpTask
     */
    @RequestMapping(value = "deleteHttpTask", produces = "application/json;charset=utf-8")
    public View<Boolean> deleteHttpTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpTaskServer.deleteTask(id));
    }

    /**
     * 更具taskId获取httpTask
     */
    @RequestMapping(value = "searchTaskOne", produces = "application/json;charset=utf-8")
    public View<Task> searchTaskOne(
            @RequestParam(value = "taskId", required = true) Long taskId,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Task>(-10086);
        return new View<Task>(httpTaskServer.queryById(taskId));
    }

    /**
     * 更具taskId立即执行Task
     */
    @RequestMapping(value = "taskImmediateExecution", produces = "application/json;charset=utf-8")
    public View<Boolean> taskImmediateExecution(
            @RequestParam(value = "taskId", required = true) Long taskId,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        return new View<Boolean>(httpTaskServer.immediateExecution(taskId));
    }

    /**
     * 接受分发任务HttpHandOutPattern
     */
    @RequestMapping(value = "createOrUpdateHandleTask", produces = "application/json;charset=utf-8")
    public View<Boolean> createHandleOutTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "executeTime", required = true) String executeTime,
            @RequestParam(value = "concurrencyNumber", required = false) Integer concurrencyNumber,
            @RequestParam(value = "parameterType", required = false) Integer parameterType,
            @RequestParam(value = "sourceURL", required = true) String sourceURL,
            @RequestParam(value = "sourceType", required = false) String sourceType,
            @RequestParam(value = "destinationURL", required = true) String destinationURL,
            @RequestParam(value = "destinationType", required = false) String destinationType,
            @RequestParam(value = "executeType", required = true) Integer executeType,
            @RequestParam(value = "executeUnit", required = true) Integer executeUnit,
            @RequestParam(value = "overTime", required = false) Integer overTime,
            @RequestParam(value = "handleTime", required = false) Integer handleTime,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(httpHandOutPatternService.createTask(concurrencyNumber, name, parameterType, sourceURL, sourceType, destinationURL, destinationType, executeType, executeUnit, overTime, handleTime, executeTime));
        else
            return new View<Boolean>(httpHandOutPatternService.updateTask(id, concurrencyNumber, name, parameterType, sourceURL, sourceType, destinationURL, destinationType, executeType, executeUnit, overTime, handleTime, executeTime));
    }

    /**
     * 开启分发任务
     */
    @RequestMapping(value = "startHandleTask", produces = "application/json;charset=utf-8")
    public View<Boolean> startHandleTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpHandOutPatternService.startTask(id));
    }

    /**
     * 关闭分发任务
     */
    @RequestMapping(value = "closeHandleTask", produces = "application/json;charset=utf-8")
    public View<Boolean> closeHandleTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpHandOutPatternService.closeTask(id));
    }

    /**
     * 删除分发任务
     */
    @RequestMapping(value = "deleteHandleTask", produces = "application/json;charset=utf-8")
    public View<Boolean> deleteHandleTask(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        if (id == null)
            return new View<Boolean>(false);
        else
            return new View<Boolean>(httpHandOutPatternService.deleteTask(id));
    }

    /**
     * 更具taskId获取handleTask
     */
    @RequestMapping(value = "searchHandleTaskOne", produces = "application/json;charset=utf-8")
    public View<HandOut> searchHandleTaskOne(
            @RequestParam(value = "taskId", required = true) Long taskId,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<HandOut>(-10086);
        return new View<HandOut>(httpHandOutPatternService.queryById(taskId));
    }

    /**
     * 更具taskId立即执行Task
     */
    @RequestMapping(value = "handOutTaskImmediateExecution", produces = "application/json;charset=utf-8")
    public View<Boolean> handOutTaskImmediateExecution(
            @RequestParam(value = "taskId", required = true) Long taskId,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security))
            return new View<Boolean>(-10086);
        return new View<Boolean>(httpHandOutPatternService.immediateExecution(taskId));
    }

}
