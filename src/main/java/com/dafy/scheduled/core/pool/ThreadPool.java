package com.dafy.scheduled.core.pool;

import com.dafy.scheduled.core.process.AbstractTaskProcess;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * mysql版调度池
 *
 * @author 付成垒
 */
public class ThreadPool
{

    private static Logger logger = Logger.getLogger(ThreadPool.class);

    private static ThreadPool threadPool = new ThreadPool();

    private static LinkedBlockingDeque blockingDeque;

    private ThreadPool()
    {
        blockingDeque = new LinkedBlockingDeque<Runnable>();
        this.service = new ThreadPoolExecutor(Integer.parseInt(AbstractTaskProcess.scheduled.getString("taskPoolCoreSize")), Integer.parseInt(AbstractTaskProcess.scheduled.getString("taskPoolMaxSize")), Long.parseLong(AbstractTaskProcess.scheduled.getString("taskPoolAliveTime")), TimeUnit.SECONDS, blockingDeque);
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
        result.put("_waitNum", threadPool.service.getQueue().size());
        result.put("_coreNum", threadPool.service.getCorePoolSize());
        result.put("_maxNum", threadPool.service.getMaximumPoolSize());
        result.put("_currentTaskNum", threadPool.service.getActiveCount());
        result.put("_currentFinishedNum", threadPool.service.getCompletedTaskCount());
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
