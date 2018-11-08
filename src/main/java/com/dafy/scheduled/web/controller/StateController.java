package com.dafy.scheduled.web.controller;

import com.dafy.scheduled.core.View;
import com.dafy.scheduled.core.service.SwitchService;
import com.dafy.scheduled.web.util.SecurityCheck;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信号量
 *
 * @author 付成垒
 */
@RestController
@RequestMapping(value = "semaphore")
public class StateController
{

    @Autowired
    private SwitchService stateService;

    @Autowired
    private SecurityCheck securityCheck;

    private static Logger logger = Logger.getLogger(StateController.class);

    @RequestMapping(value = "showSemaphore", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public View<Integer> showSemaphore(
            @RequestParam(value = "taskType", required = true) String taskType,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security)) return new View<Integer>(-10086, -10086);
        return new View<Integer>(stateService.showSemaphore(taskType), 0);
    }

    @RequestMapping(value = "semaphoreSwitch", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public View<Boolean> semaphoreSwitch(
            @RequestParam(value = "taskType", required = false) String taskType,
            @RequestParam(value = "state", required = true) Integer state,
            @RequestParam(value = "security", required = true) String security
    )
    {
        if (!securityCheck.securityCheck(security)) return new View<Boolean>(-10086);
        if (state != 3 && state != 0) return new View<Boolean>(false);
        if (taskType == null || "".equals(taskType))
            return new View<Boolean>(stateService.semaphoreSwitchAll(state));
        return new View<Boolean>(stateService.semaphoreSwitch(taskType, state));
    }

}
