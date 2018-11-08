package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.plugin.SwitchPlugin;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;
import org.apache.log4j.Logger;

/**
 * 信号量
 *
 * @author 付成垒
 */
@ReferencePlugin
public class SwitchService
{

    private static Logger logger = Logger.getLogger(SwitchService.class);

    @Linked
    private SwitchPlugin statePlugin;

    //初始化信号量
    public boolean initSemaphore()
    {
        statePlugin.obtainAllSemaphore();
        return statePlugin.initSemaphore();
    }

    //信号量操作
    public boolean semaphoreSwitch(String taskType, Integer state)
    {
        return statePlugin.semaphoreSwitch(taskType, state);
    }

    //信号量批量操作
    public boolean semaphoreSwitchAll(Integer state)
    {
        return statePlugin.semaphoreSwitchAll(state);
    }

    //信号量查询操作
    public int showSemaphore(String taskType)
    {
        return statePlugin.showSemaphore(taskType);
    }

    //获取信号量
    public boolean obtainSemaphore(String taskType)
    {
        return statePlugin.obtainSemaphore(taskType);
    }

    //释放信号量
    public boolean freeSemaphore(String taskType)
    {
        return statePlugin.freeSemaphore(taskType);
    }
}
