package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.plugin.HandOutPlugin;
import com.dafy.scheduled.core.pool.ThreadPool;
import com.dafy.scheduled.core.process.HandOutSourceProcess;
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
 * 分发任务
 *
 * @author 付成垒
 */
@ReferencePlugin
public class HandOutService
{

    private static Logger logger = Logger.getLogger(HandOutService.class);

    @Linked
    private HandOutPlugin httpHandOutPatternPlugin;

    @Linked
    private SwitchService stateService;

    /**
     * 新增分发任务
     */
    public boolean insertHttpHandOutPattern(HandOut data)
    {
        return httpHandOutPatternPlugin.insertHttpHandOutPattern(data);
    }

    /**
     * 修改任务
     */
    public boolean updateHttpHandOutPattern(HandOut data)
    {
        return httpHandOutPatternPlugin.updateHttpHandOutPattern(data);
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    public boolean updateDataServer(HandOut data)
    {
        return httpHandOutPatternPlugin.updateDataServer(data);
    }

    /**
     * 删除任务
     */
    public boolean deleteHttpHandOutPattern(Integer id)
    {
        return httpHandOutPatternPlugin.deleteHttpHandOutPattern(id);
    }

    /**
     * 查询任务列表(暂时查询所有的任务)
     */
    public List<HandOut> searchAllHttpHandOutPattern()
    {
        return httpHandOutPatternPlugin.searchAllHttpHandOutPattern();
    }

    //查询HttpHandOutPattern通过id
    public HandOut queryById(Long id)
    {
        return httpHandOutPatternPlugin.queryById(id);
    }

    /**
     * 查询本次的任务
     */
    public List<HandOut> searchHttpHandoutPatternWillSubmit()
    {
        if (!stateService.obtainSemaphore("handOut"))
            return new ArrayList<HandOut>();
        int i = 0;
        List<HandOut> datas = httpHandOutPatternPlugin.searchHttpHandoutPatternWillSubmit();
        for (i = 0; i < datas.size(); i++)
        {
            HandOut data = datas.get(i);
            data.setnState(AbstractTaskProcess.TASK_STATE_PRE_EXECUTION);
            data.setStrMessage("预执行");
            //设置下一次执行时间
            data.setDtExecuteTime(AbstractTaskProcess.computingTime(data.getDtExecuteTime(), data.getnExecuteType(), data.getnExecuteUnit()));
            if (!this.updateHttpHandOutPattern(data))
            {
                datas.remove(i);
            }
        }
        stateService.freeSemaphore("handOut");
        return datas;
    }

    //修改单个HttpHandOutPattern状态
    public boolean updateState(Long id, Integer state, String message, String columnName, String time, String strUseTime)
    {
        HandOut data = queryById(id);
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
            data.setDtFailTime(null);
            data.setStrUseTime(strUseTime);
        }
        if (columnName == "init")
        {
            data.setDtStartTime(null);
        }
        return updateHttpHandOutPattern(data);
    }

    //修改单个HttpHandOutPattern状态
    public void writeError(Long id)
    {
        httpHandOutPatternPlugin.writeError(id);
    }

    /**
     * 本地HttpHandOutPattern初始化（启动）
     * <p>
     * 作用：1.将与执行执行中完成的错误的Task初始化为未执行
     */
    public void initHttpHandOutPattern()
    {
        httpHandOutPatternPlugin.initHttpHandOutPattern();
    }

    /**
     * 重置以失败的HttpHandOutPattern
     */
    public void resetHttpHandOutPattern()
    {
        httpHandOutPatternPlugin.resetHttpHandOutPattern();
    }

    //开启任务
    public boolean startTask(Long id)
    {
        HandOut data = this.queryById(id);
        if (data == null)
            return false;
        data.setnState(0);
        data.setStrMessage("未执行");
        return httpHandOutPatternPlugin.startOrCloseTask(data, 5);
    }

    //关闭任务
    public boolean closeTask(Long id)
    {
        HandOut data = this.queryById(id);
        if (data == null)
            return false;
        data.setnState(5);
        data.setStrMessage("暂停中");
        return httpHandOutPatternPlugin.startOrCloseTask(data, 0);
    }

    //删除任务
    public boolean deleteTask(Long id)
    {
        HandOut data = this.queryById(id);
        if (data == null)
            return false;
        return httpHandOutPatternPlugin.deleteTask(data);
    }

    //穿件HttpTask
    public boolean createTask(Integer concurrencyNumber, String name, Integer parameterType, String sourceURL, String sourceType, String destinationURL, String destinationType, Integer executeType, Integer executeUnit, Integer overTime, Integer handleTime, String executeTime)
    {
        HandOut httpHandOutPatternMataData = new HandOut();
        httpHandOutPatternMataData.setStrName(name);
        httpHandOutPatternMataData.setnState(5);
        httpHandOutPatternMataData.setStrMessage("暂停中");
        httpHandOutPatternMataData.setDtCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        httpHandOutPatternMataData.setStrSourceURL(sourceURL);
        if (concurrencyNumber == null)
            httpHandOutPatternMataData.setnConcurrencyNumber(1);
        else
            httpHandOutPatternMataData.setnConcurrencyNumber(concurrencyNumber);
        if (parameterType == null)
            httpHandOutPatternMataData.setnParameterType(1);
        else
            httpHandOutPatternMataData.setnParameterType(parameterType);
        if (sourceType == null || "POST".equals(sourceType))
            httpHandOutPatternMataData.setStrSourceType("POST");
        else
            httpHandOutPatternMataData.setStrSourceType("GET");
        httpHandOutPatternMataData.setStrDestinationURL(destinationURL);
        if (destinationType == null || "POST".equals(destinationType))
            httpHandOutPatternMataData.setStrDestinationType("POST");
        else
            httpHandOutPatternMataData.setStrDestinationType("GET");
        if (overTime == null)
            httpHandOutPatternMataData.setnOverTime(5000);
        else
            httpHandOutPatternMataData.setnOverTime(overTime);
        if (handleTime == null)
            httpHandOutPatternMataData.setnHandleTime(5000);
        else
            httpHandOutPatternMataData.setnHandleTime(handleTime);
        httpHandOutPatternMataData.setDtExecuteTime(executeTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        httpHandOutPatternMataData.setnExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        httpHandOutPatternMataData.setnExecuteUnit(executeUnit);
        if (!this.insertHttpHandOutPattern(httpHandOutPatternMataData))
        {
            return false;
        }
        return true;
    }

    //更新httpTask
    public boolean updateTask(Long id, Integer concurrencyNumber, String name, Integer parameterType, String sourceURL, String sourceType, String destinationURL, String destinationType, Integer executeType, Integer executeUnit, Integer overTime, Integer handleTime, String executeTime)
    {
        HandOut date = this.queryById(id);
        if (date == null)
            return false;
        date.setStrName(name);
        date.setStrMessage("未执行");
        date.setStrSourceURL(sourceURL);
        if (concurrencyNumber == null)
            date.setnConcurrencyNumber(1);
        else
            date.setnConcurrencyNumber(concurrencyNumber);
        if (parameterType == null)
            date.setnParameterType(1);
        else
            date.setnParameterType(parameterType);
        if (sourceType == null || "POST".equals(sourceType))
            date.setStrSourceType("POST");
        else
            date.setStrSourceType("GET");
        date.setStrDestinationURL(destinationURL);
        if (destinationType == null || "POST".equals(destinationType))
            date.setStrDestinationType("POST");
        else
            date.setStrDestinationType("GET");
        if (overTime == null)
            date.setnOverTime(5000);
        else
            date.setnOverTime(overTime);
        if (handleTime == null)
            date.setnHandleTime(5000);
        else
            date.setnHandleTime(handleTime);
        date.setDtExecuteTime(executeTime);
        if (overTime != null)
            date.setnOverTime(overTime);
        if (executeType == null || "1,2,3,4,5,6,7".indexOf(String.valueOf(executeType)) == -1)
            return false;
        date.setnExecuteType(executeType);
        if (executeUnit == null || executeUnit == 0)
            return false;
        date.setnExecuteUnit(executeUnit);
        if (!this.updateDataServer(date))
        {
            return false;
        }
        return true;
    }

    //立即执行 只有暂停中的任务才能立即执行
    public boolean immediateExecution(Long id)
    {
        HandOut data = this.queryById(id);
        if (data == null) return false;
        if (data.getnState().intValue() != 5) return false;
        ThreadPool.obtainService().submit(new ProxyInstance<HandOutSourceProcess>().newInstance(new HandOutSourceProcess(data, false)));
        return true;
    }

}
