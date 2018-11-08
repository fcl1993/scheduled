package com.dafy.scheduled.core.process;

import com.dafy.scheduled.core.http.HttpRequest;
import com.dafy.scheduled.core.http.HttpReturn;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.service.TaskService;
import com.fuchenglei.core.container.Container;
import com.fuchenglei.util.SerializableUtil;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * httpTask
 *
 * @author 付成垒
 */
public class TaskProcess extends AbstractTaskProcess implements Callable<ProcessResult>
{

    private static Logger logger = Logger.getLogger(TaskProcess.class);

    private TaskService httpTaskService = Container.obtainBean(TaskService.class);

    private Task metaData;

    private String message;

    public TaskProcess(Task metaData, boolean type)
    {
        super();
        super.type = type;
        this.metaData = metaData;
    }

    public ProcessResult call()
    {
        long startTime = System.currentTimeMillis();
        if (!super.type)
            logger.info("手动调用开始==> 任务类型: " + metaData.getClass().getSimpleName() + " 任务信息: " + new SerializableUtil().toJson(metaData));
        if (super.type)
        {
            //修改task状态到执行中
            this.message = "执行中";
            this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_RUNNING, this.message, "startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime), null);
            //任务开始
            this.state = TASK_STATE_RUNNING;
        }
        executor();
        long endTime = System.currentTimeMillis();
        ProcessResult result = new ProcessResult(this.message, super.computingUseTime(startTime, endTime), super.type);
        if (super.type)
        {
            if (this.state == TASK_STATE_EXCEPTION)
            {
                this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_EXCEPTION, this.message, "failTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime), result.getTime());
                this.httpTaskService.writeError(this.metaData.getlId());
            }
            if (this.state == TASK_STATE_COMPLETE)
            {
                this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_COMPLETE, this.message, "finishedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime), result.getTime());
                this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_BEFORE, "未执行", "init", null, null);
            }
        }
        return result;
    }

    @Override
    public void executor()
    {
        try
        {
            HttpReturn httpReturn = null;
            Map mp = new SerializableUtil<HashMap>().toObject(HashMap.class, this.metaData.getStrArgs());
            if ("GET".equals(this.metaData.getStrType().trim()))
            {
                String url = "";
                if (!mp.isEmpty())
                {
                    StringBuilder builder = new StringBuilder("");
                    builder.append("?");
                    Iterator<String> keys = mp.keySet().iterator();
                    while (keys.hasNext())
                    {
                        String key = keys.next();
                        builder.append(key + "=" + URLEncoder.encode(String.valueOf(mp.get(key)), "utf-8") + "&");
                    }
                    url = builder.toString();
                    url = url.substring(0, url.length() - 1);
                }
                httpReturn = HttpRequest.requestGet(this.metaData.getStrUrl() + url, this.metaData.getnOverTime());
            }
            else if ("POST".equals(this.metaData.getStrType().trim()))
            {
                httpReturn = HttpRequest.requestPost(this.metaData.getStrUrl(), mp, this.metaData.getnOverTime());
            }
            if (httpReturn.getCode() == 0)
            {
                this.state = TASK_STATE_COMPLETE;
                this.message = httpReturn.getMessage();
            }
            else
            {
                this.message = httpReturn.getMessage();
                this.state = TASK_STATE_EXCEPTION;
            }
        }
        catch (Exception exception)
        {
            this.state = TASK_STATE_EXCEPTION;
            this.message = "本次任务异常请检查任务配置";
        }
    }

}
