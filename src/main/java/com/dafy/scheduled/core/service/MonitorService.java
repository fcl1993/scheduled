package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.View;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.data.Job;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.plugin.MonitorPlugin;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

@ReferencePlugin
public class MonitorService
{

    private Logger logger = Logger.getLogger(MonitorService.class);

    @Linked
    private MonitorPlugin monitorPlugin;

    //获取当前task状态
    public HashMap<String, Object> getTaskState()
    {
        HashMap<String, Object> mp = monitorPlugin.getTaskStateHandOut();
        mp.putAll(monitorPlugin.getTaskState());
        return mp;
    }

    //获取localJob列表
    public View<List<Job>> getListJob(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        return monitorPlugin.getListJob(id, state, name, currentPage, pageSize);
    }

    //获取localJob成功列表100调
    public View<List<Job>> getListJobSuccess(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        return monitorPlugin.getListJobSuccess(id, state, name, currentPage, pageSize);
    }

    //获取localJob失败列表100调
    public View<List<Job>> getListJobFail(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        return monitorPlugin.getListJobFail(id, state, name, currentPage, pageSize);
    }

    //获取task列表
    public View<List<Task>> getListTask(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        return monitorPlugin.getListTask(id, state, name, currentPage, pageSize);
    }

    //获取task列表
    public View<List<HandOut>> getHandOutListTask(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        return monitorPlugin.getHandOutListTask(id, state, name, currentPage, pageSize);
    }

}
