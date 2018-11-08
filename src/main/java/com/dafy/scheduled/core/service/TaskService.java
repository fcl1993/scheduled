package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.plugin.TaskPlugin;
import com.dafy.scheduled.core.pool.ThreadPool;
import com.dafy.scheduled.core.process.TaskProcess;
import com.dafy.scheduled.core.process.AbstractTaskProcess;
import com.dafy.scheduled.core.proxy.ProxyInstance;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * http Task 服务
 */
@ReferencePlugin
public class TaskService
{

    private static Logger logger = Logger.getLogger(TaskService.class);

    @Linked
    private TaskPlugin httpTaskPlugin;

    @Linked
    private SwitchService stateService;

    //新增本地调用任务Task
    public boolean insertData(Task data)
    {
        return httpTaskPlugin.insertData(data);
    }

    //修改本地调用任务Task
    public boolean updateData(Task data)
    {
        return httpTaskPlugin.updateData(data);
    }

    //删除本地调用任务Task
    public boolean deleteData(Long id)
    {
        return httpTaskPlugin.deleteData(id);
    }

    //查询localTask通过id
    public Task queryById(Long id)
    {
        return httpTaskPlugin.queryById(id);
    }

    //查询需要执行的Task添加到执行队列
    public List<Task> querySoonExecuteLocalTask()
    {
        if (!stateService.obtainSemaphore("task"))
            return new ArrayList<Task>();
        int i = 0;
        List<Task> datas = httpTaskPlugin.querySoonExecuteLocalTask();
        for (i = 0; i < datas.size(); i++)
        {
            Task data = datas.get(i);
            data.setnState(AbstractTaskProcess.TASK_STATE_PRE_EXECUTION);
            data.setStrMessage("预执行");
            data.setDtExecuteTime(AbstractTaskProcess.computingTime(data.getDtExecuteTime(), data.getnExecuteType(), data.getnExecuteUnit()));
            if (!this.updateData(data))
            {
                datas.remove(i);
            }
        }
        stateService.freeSemaphore("task");
        return datas;
    }

    //修改单个Task状态
    public boolean updateState(Long id, Integer state, String message, String columnName, String time, String strUseTime)
    {
        Task data = queryById(id);
        if (data == null)
            return false;
        data.setnState(state);
        data.setStrMessage(message);
        if (columnName == "startTime")
            data.setDtStartTime(time);
        if (columnName == "failTime")
        {
            data.setDtFailTime(time);
            data.setStrUseTime(strUseTime);
        }
        if (columnName == "finishedTime")
        {
            data.setDtFinishedTime(time);
            data.setDtFailTime(null);
            data.setStrUseTime(strUseTime);
        }
        if (columnName == "init")
            data.setDtStartTime(null);
        return updateData(data);
    }

    //修改单个Task状态
    public void writeError(Long id)
    {
        httpTaskPlugin.writeError(id);
    }

    /**
     * 本地Task初始化（启动）
     * <p>
     * 作用：1.将与执行执行中完成的错误的Task初始化为未执行
     */
    public void initTask()
    {
        httpTaskPlugin.initTask();
    }

    /**
     * 重置以失败的task
     */
    public void resetHttpTask()
    {
        httpTaskPlugin.resetHttpTask();
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    public boolean updateDataServer(Task data)
    {
        return httpTaskPlugin.updateDataServer(data);
    }

    //开启任务
    public boolean startTask(Long id)
    {
        Task data = this.queryById(id);
        if (data == null)
            return false;
        data.setnState(0);
        data.setStrMessage("未执行");
        return httpTaskPlugin.startOrCloseTask(data, 5);
    }

    //关闭任务
    public boolean closeTask(Long id)
    {
        Task data = this.queryById(id);
        if (data == null)
            return false;
        data.setnState(5);
        data.setStrMessage("暂停中");
        return httpTaskPlugin.startOrCloseTask(data, 0);
    }

    //删除任务
    public boolean deleteTask(Long id)
    {
        Task data = this.queryById(id);
        if (data == null)
            return false;
        return httpTaskPlugin.deleteTask(data);
    }

    //穿件HttpTask
    public boolean createHttpTask(String executeTime, String name, String url, String type, Integer executeType, Integer executeUnit, Integer overTime, String args)
    {
        Date date = new Date();
        Task httpTaskMataData = new Task();
        httpTaskMataData.setnState(5);
        httpTaskMataData.setStrName(name);
        httpTaskMataData.setStrMessage("暂停中");
        httpTaskMataData.setStrUrl(url);
        httpTaskMataData.setDtCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        httpTaskMataData.setStrArgs(args);
        httpTaskMataData.setnOverTime(overTime == null ? 5000 : overTime);
        if (!"GET".equals(type))
            httpTaskMataData.setStrType("POST");
        else
            httpTaskMataData.setStrType("GET");
        if (executeTime != null && !executeTime.equals(""))
            httpTaskMataData.setDtExecuteTime(executeTime);
        else
            httpTaskMataData.setDtExecuteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        if (overTime != null)
            httpTaskMataData.setnOverTime(overTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        httpTaskMataData.setnExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        httpTaskMataData.setnExecuteUnit(executeUnit);
        if (!this.insertData(httpTaskMataData))
            return false;
        return true;
    }

    //更新httpTask
    public boolean updateHttpTask(Long id, String executeTime, String name, String url, String type, Integer executeType, Integer executeUnit, Integer overTime, String args)
    {
        Task data = this.queryById(id);
        if (data == null)
            return false;
        data.setStrName(name);
        data.setStrUrl(url);
        data.setStrArgs(args);
        data.setnOverTime(overTime == null ? 5000 : overTime);
        if ("POST".equals(type))
            data.setStrType("POST");
        else
            data.setStrType("GET");
        if (executeTime != null && !executeTime.equals(""))
            data.setDtExecuteTime(executeTime);
        if (overTime != null)
            data.setnOverTime(overTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        data.setnExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        data.setnExecuteUnit(executeUnit);
        if (!this.updateDataServer(data))
            return false;
        return true;
    }

    //立即执行 只有暂停中的任务才能立即执行
    public boolean immediateExecution(Long id)
    {
        Task data = this.queryById(id);
        if (data == null) return false;
        if (data.getnState().intValue() != 5) return false;
        ThreadPool.obtainService().submit(new ProxyInstance<TaskProcess>().newInstance(new TaskProcess(data, false)));
        return true;
    }

}
