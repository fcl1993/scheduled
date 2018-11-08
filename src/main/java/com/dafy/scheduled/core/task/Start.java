package com.dafy.scheduled.core.task;

import com.dafy.scheduled.core.scheme.DataCenter;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.data.Job;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.pool.ThreadPool;
import com.dafy.scheduled.core.process.ProcessResult;
import com.dafy.scheduled.core.proxy.ProxyInstance;
import com.dafy.scheduled.core.service.*;
import com.dafy.scheduled.core.process.HandOutSourceProcess;
import com.dafy.scheduled.core.process.JobProcess;
import com.dafy.scheduled.core.process.TaskProcess;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.runner.Grade;
import com.fuchenglei.core.runner.RunnerLoop;
import com.fuchenglei.core.runner.RunnerOnce;
import com.fuchenglei.core.runner.RunnerPlugin;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.Future;

/**
 * 启动项目
 *
 * @author 付成垒
 */
@RunnerPlugin
public class Start
{

    private static Logger logger = Logger.getLogger(Start.class);

    @Linked
    private DataCenter dataCenter;

    @Linked
    private JobService httpJobService;

    @Linked
    private TaskService httpTaskServer;

    @Linked
    private HandOutService httpHandOutPatternService;

    @Linked
    private SwitchService stateService;

    @Linked
    private DictionaryService dictionaryService;

    @RunnerOnce
    @Grade(1)
    public void DataCenterRunner()
    {
        dataCenter.tableInit();
        stateService.initSemaphore();
    }

    @RunnerOnce
    @Grade(value = 2)
    public void TaskInit()
    {
        httpJobService.initJob();
        httpTaskServer.initTask();
        httpHandOutPatternService.initHttpHandOutPattern();
    }

    @RunnerLoop(time = 1)
    public void HttpJobPostRunner()
    {
        try
        {
            if (httpJobService.localJobTimingProcessing())
            {
                Thread.sleep(1000);
            }
            else
            {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @RunnerLoop(time = 1)
    public void HttpJobStart()
    {
        try
        {
            if (addHttpJob())
            {
                Thread.sleep(1000);
            }
            else
            {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @RunnerLoop(time = 1)
    public void HttpTaskStart()
    {
        try
        {
            if (addHttpTask())
            {
                Thread.sleep(1000);
            }
            else
            {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @RunnerLoop(time = 1)
    public void HttpHandOutPatternStart()
    {
        try
        {
            if (addHttpHandOutPattern())
            {
                Thread.sleep(1000);
            }
            else
            {
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    //一天换一次密码
    @RunnerLoop(time = 24 * 60 * 60 * 1000)
    public void initSecurity()
    {
        dictionaryService.initSecurity();
    }

    /**
     * 增加本地Job
     */
    public boolean addHttpJob()
    {
        List<Job> data = httpJobService.querySoonExecuteLocalJob();
        if (data.size() == 0)
            return false;
        for (Job arg : data)
        {
            try
            {
                Future<ProcessResult> future = ThreadPool.obtainService().submit(new ProxyInstance<JobProcess>().newInstance(new JobProcess(arg, true)));
            }
            catch (Exception e)
            {
                logger.error("添加httpJob失败! job id:" + arg.getlId());
                continue;
            }
        }
        return true;
    }

    /**
     * 增加本地Task
     */
    public boolean addHttpTask()
    {
        List<Task> data = httpTaskServer.querySoonExecuteLocalTask();
        if (data.size() == 0)
            return false;
        for (Task arg : data)
        {
            try
            {
                Future<ProcessResult> future = ThreadPool.obtainService().submit(new ProxyInstance<TaskProcess>().newInstance(new TaskProcess(arg, true)));
            }
            catch (Exception e)
            {
                logger.error("添加本地Task失败! task id:" + arg.getlId());
                continue;
            }
        }
        return true;
    }

    /**
     * 增加本地Task
     */
    public boolean addHttpHandOutPattern()
    {
        List<HandOut> datas = httpHandOutPatternService.searchHttpHandoutPatternWillSubmit();
        if (datas.size() == 0)
            return false;
        for (HandOut data : datas)
        {
            try
            {
                Future<ProcessResult> future = ThreadPool.obtainService().submit(new ProxyInstance<HandOutSourceProcess>().newInstance(new HandOutSourceProcess(data, true)));
            }
            catch (Exception e)
            {
                logger.error("添加本地HttpHandOutPattern失败! task id:" + data.getlId());
                continue;
            }
        }
        return true;
    }

}
