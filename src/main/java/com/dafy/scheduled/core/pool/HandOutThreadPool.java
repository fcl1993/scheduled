package com.dafy.scheduled.core.pool;

import com.dafy.scheduled.core.process.AbstractTaskProcess;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 分发池
 *
 * @author 付成垒
 */
public class HandOutThreadPool
{

    private static Logger logger = Logger.getLogger(HandOutThreadPool.class);

    private static HandOutThreadPool threadPool = new HandOutThreadPool();

    private static LinkedBlockingDeque blockingDeque;

    private HandOutThreadPool()
    {
        blockingDeque = new LinkedBlockingDeque<Runnable>();
        this.service = new ThreadPoolExecutor(Integer.parseInt(AbstractTaskProcess.scheduled.getString("handOutPoolCoreSize")), Integer.parseInt(AbstractTaskProcess.scheduled.getString("handOutPoolMaxSize")), Long.parseLong(AbstractTaskProcess.scheduled.getString("handOutPoolAliveTime")), TimeUnit.SECONDS, blockingDeque);
    }

    /**
     * 线程池
     */
    private ThreadPoolExecutor service;

    /**
     * 暴露队列
     */
    public static LinkedBlockingDeque obtainQueue()
    {
        if (blockingDeque == null)
        {
            blockingDeque = new LinkedBlockingDeque<Runnable>();
        }
        return threadPool.blockingDeque;
    }

    /**
     * 获取当前线程池状态
     */
    public static HashMap<String, Object> obtainTaskState()
    {
        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("waitNum", threadPool.service.getQueue().size());
        result.put("coreNum", threadPool.service.getCorePoolSize());
        result.put("maxNum", threadPool.service.getMaximumPoolSize());
        result.put("currentTaskNum", threadPool.service.getActiveCount());
        result.put("currentFinishedNum", threadPool.service.getCompletedTaskCount());
        return result;
    }

    /**
     * 暴露线程池
     */
    public static ThreadPoolExecutor obtainService()
    {
        if (threadPool.service.isTerminated())
        {
            logger.error("thread pool start fail ..");
        }
        return threadPool.service;
    }

}
