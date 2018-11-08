package com.dafy.scheduled.web.controller;

import com.dafy.scheduled.core.View;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.data.Job;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.service.MonitorService;
import com.dafy.scheduled.web.util.SecurityCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 监控
 *
 * @author 付成垒
 */
@RestController
@RequestMapping(value = "monitor")
public class MonitorController
{

    @Autowired
    private MonitorService monitorService;

    @Autowired
    private SecurityCheck securityCheck;

    @RequestMapping(value = "getTaskState")
    public View<HashMap<String, Object>> getTaskState
            (
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<HashMap<String, Object>>(-10086);
        return new View(monitorService.getTaskState());
    }

    @RequestMapping(value = "getListJob")
    public View<List<Job>> getListJob
            (
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "state", required = false) Integer state,
                    @RequestParam(value = "name", required = false) String name,
                    @RequestParam(value = "currentPage", required = true) Integer currentPage,
                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<List<Job>>(-10086);
        return monitorService.getListJob(id, state, name, currentPage, pageSize);
    }

    @RequestMapping(value = "getListSuccessHttpJob")
    public View<List<Job>> getListSuccessHttpJob
            (
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "state", required = false) Integer state,
                    @RequestParam(value = "name", required = false) String name,
                    @RequestParam(value = "currentPage", required = true) Integer currentPage,
                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<List<Job>>(-10086);
        return monitorService.getListJobSuccess(id, state, name, currentPage, pageSize);
    }

    @RequestMapping(value = "getListFailHttpJob")
    public View<List<Job>> getListFailHttpJob
            (
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "state", required = false) Integer state,
                    @RequestParam(value = "name", required = false) String name,
                    @RequestParam(value = "currentPage", required = true) Integer currentPage,
                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<List<Job>>(-10086);
        return monitorService.getListJobFail(id, state, name, currentPage, pageSize);
    }

    @RequestMapping(value = "getListHttpTask")
    public View<List<Task>> getListHttpTask
            (
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "state", required = false) Integer state,
                    @RequestParam(value = "name", required = false) String name,
                    @RequestParam(value = "currentPage", required = true) Integer currentPage,
                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<List<Task>>(-10086);
        return monitorService.getListTask(id, state, name, currentPage, pageSize);
    }

    @RequestMapping(value = "getHandOutListTask")
    public View<List<HandOut>> getHandOutListTask
            (
                    @RequestParam(value = "id", required = false) Long id,
                    @RequestParam(value = "state", required = false) Integer state,
                    @RequestParam(value = "name", required = false) String name,
                    @RequestParam(value = "currentPage", required = true) Integer currentPage,
                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                    @RequestParam(value = "security", required = true) String security
            )
    {
        if (!securityCheck.securityCheck(security)) return new View<List<HandOut>>(-10086);
        return monitorService.getHandOutListTask(id, state, name, currentPage, pageSize);
    }

}
