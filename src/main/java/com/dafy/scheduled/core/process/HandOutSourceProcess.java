package com.dafy.scheduled.core.process;

import com.dafy.scheduled.core.http.HttpRequest;
import com.dafy.scheduled.core.http.HttpReturn;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.pool.HandOutThreadPool;
import com.dafy.scheduled.core.service.HandOutService;
import com.fuchenglei.core.container.Container;
import com.fuchenglei.util.SerializableUtil;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * httpTask
 *
 * @author 付成垒
 */
public class HandOutSourceProcess extends AbstractTaskProcess implements Callable<ProcessResult>
{

    private static Logger logger = Logger.getLogger(HandOutSourceProcess.class);

    private HandOutService httpTaskService = Container.obtainBean(HandOutService.class);

    private HandOut metaData;

    private String message;

    private LinkedBlockingDeque<Boolean> queue;

    public HandOutSourceProcess(HandOut metaData, boolean type)
    {
        super();
        super.type = type;
        this.queue = new LinkedBlockingDeque(metaData.getnConcurrencyNumber());
        this.metaData = metaData;
    }

    public ProcessResult call()
    {
        long startTime = System.currentTimeMillis();
        if (!super.type)
            logger.info("手动调用开始==> 任务类型: " + metaData.getClass().getSimpleName() + " 任务信息: " + new SerializableUtil().toJson(metaData));
        if (super.type)
        {
            //修改job状态到执行中
            this.message = "执行中";
            this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_RUNNING, this.message, "dtStartTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime), null);
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
                this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_EXCEPTION, this.message, "dtFailTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime), result.getTime());
                this.httpTaskService.writeError(this.metaData.getlId());
            }
            if (this.state == TASK_STATE_COMPLETE)
            {
                this.httpTaskService.updateState(this.metaData.getlId(), TASK_STATE_COMPLETE, this.message, "dtFinishedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime), result.getTime());
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
            while (true)
            {
                HttpReturn httpReturn = null;
                if ("GET".equals(this.metaData.getStrSourceType().trim()))
                {
                    httpReturn = HttpRequest.requestHttpHandOutPatternGet(this.metaData.getStrSourceURL(), this.metaData.getnOverTime());
                }
                else if ("POST".equals(this.metaData.getStrSourceType().trim()))
                {
                    httpReturn = HttpRequest.requestHttpHandOutPatternPost(this.metaData.getStrSourceURL(), new HashMap(), this.metaData.getnOverTime());
                }
                if (httpReturn.getCode() == 0)
                {
                    this.state = TASK_STATE_COMPLETE;
                    this.message = httpReturn.getMessage();
                    //获取数据hand out
                    if (httpReturn.getData().size() == 0)
                        break;
                    this.handOut(httpReturn.getData());
                }
                else
                {
                    this.message = httpReturn.getMessage();
                    this.state = TASK_STATE_EXCEPTION;
                    break;
                }
            }
        }
        catch (Exception exception)
        {
            this.state = TASK_STATE_EXCEPTION;
            this.message = "本次任务异常请检查任务配置";
        }
    }

    //任务执行分发器
    public void handOut(List<Object> datas) throws InterruptedException
    {
        int i = 0;
        for (i = 0; i < datas.size(); i++)
        {
            //提交handle
            this.queue.put(true);
            HandOutDestinationProcess destination = new HandOutDestinationProcess(this.metaData, new SerializableUtil().toJson(datas.get(i)), this.queue);
            HandOutThreadPool.obtainService().submit(destination);
        }
    }

}
