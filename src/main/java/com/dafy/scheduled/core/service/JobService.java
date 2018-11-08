package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.data.Job;
import com.dafy.scheduled.core.plugin.JobPlugin;
import com.dafy.scheduled.core.pool.ThreadPool;
import com.dafy.scheduled.core.process.JobProcess;
import com.dafy.scheduled.core.process.AbstractTaskProcess;
import com.dafy.scheduled.core.proxy.ProxyInstance;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * httpJob 服务
 *
 * @author 付成垒
 */
@ReferencePlugin
public class JobService
{

    private static Logger logger = Logger.getLogger(JobService.class);

    @Linked
    private JobPlugin httpJobPlugin;

    @Linked
    private SwitchService stateService;

    //新增本地调用任务
    public boolean insertData(Job data)
    {
        return httpJobPlugin.insertData(data);
    }

    //修改本地调用任务
    public boolean updateData(Job data)
    {
        return httpJobPlugin.updateData(data);
    }

    //删除本地调用任务
    public boolean deleteData(Long id)
    {
        return httpJobPlugin.deleteData(id);
    }

    //查询localJob通过id
    public Job queryById(Long id)
    {
        return httpJobPlugin.queryById(id);
    }

    //查询失败localJob通过id
    public Job queryFailById(Long id)
    {
        return httpJobPlugin.queryFailById(id);
    }

    //查询成功localJob通过id
    public Job querySuccessById(Long id)
    {
        return httpJobPlugin.querySuccessById(id);
    }

    //查询需要执行的job添加到执行队列
    public List<Job> querySoonExecuteLocalJob()
    {
        if (!stateService.obtainSemaphore("job"))
            return new ArrayList<Job>();
        int i = 0;
        List<Job> datas = httpJobPlugin.querySoonExecuteLocalJob();
        for (i = 0; i < datas.size(); i++)
        {
            Job data = datas.get(i);
            data.setnState(AbstractTaskProcess.TASK_STATE_PRE_EXECUTION);
            data.setStrMessage("预执行");
            if (!this.updateData(data))
            {
                datas.remove(i);
            }
        }
        stateService.freeSemaphore("job");
        return datas;
    }

    //修改单个job状态
    public boolean updateState(Long id, Integer state, String message, String columnName, String time, String strUseTime)
    {
        Job data = queryById(id);
        if (data == null)
            return false;
        data.setnState(state);
        data.setStrMessage(message);
        if (columnName == "dtStartTime")
            data.setDtStartTime(time);
        if (columnName == "dtFailTime")
        {
            data.setDtFailTime(time);
            data.setStrUseTime(strUseTime);
        }
        if (columnName == "dtFinishedTime")
        {
            data.setDtFinishedTime(time);
            data.setStrUseTime(strUseTime);
        }
        return updateData(data);
    }

    /**
     * 本地job初始化（启动）
     * <p>
     * 作用：1.将与执行的job初始化为未执行
     */
    public void initJob()
    {
        httpJobPlugin.initJob();
    }

    /**
     * 获取job通过state
     */
    public List<Job> obtainLocalJobByState(Integer state)
    {
        return httpJobPlugin.obtainLocalJobByState(state);
    }

    //写入数据到记录
    public void writeData(List<Job> datas, String table)
    {
        for (Job data : datas)
            httpJobPlugin.writeData(data, table);
    }

    /**
     * 过期和失败job处理
     */
    public boolean localJobTimingProcessing()
    {
        if (!stateService.obtainSemaphore("jobHandle"))
            return false;
        writeData(obtainLocalJobByState(AbstractTaskProcess.TASK_STATE_EXCEPTION), "tbJobFail");
        writeData(obtainLocalJobByState(AbstractTaskProcess.TASK_STATE_COMPLETE), "tbJobSuccess");
        stateService.freeSemaphore("jobHandle");
        return true;
    }

    //立即执行 只有暂停中的任务才能立即执行   1:job 2:jobFail 3:jobSuccess
    public boolean immediateExecution(Long id, int type)
    {
        Job data = null;
        if (type == 1)
            data = this.queryById(id);
        else if (type == 2)
            data = this.queryFailById(id);
        else if (type == 3)
            data = this.querySuccessById(id);
        else
            return false;
        if (data == null) return false;
        ThreadPool.obtainService().submit(new ProxyInstance<JobProcess>().newInstance(new JobProcess(data, false)));
        return true;
    }

}
