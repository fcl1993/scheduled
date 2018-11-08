package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.data.HandOut;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 分发服务
 */
@ServicePlugin
public class HandOutPlugin
{

    private static Logger logger = Logger.getLogger(HandOutPlugin.class);

    /**
     * 新增分发任务
     */
    @Transaction
    public boolean insertHttpHandOutPattern(HandOut data)
    {
        return DBQuery.query().insert("insert into tbHandOut ( strName , nState, nConcurrencyNumber , strMessage , nOverTime , nHandleTime , nExecuteType , nExecuteUnit , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nParameterType , strSourceURL , strSourceType , strDestinationURL , strDestinationType , strUseTime ) " +
                        "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )",
                data.getStrName(), data.getnState(), data.getnConcurrencyNumber(), data.getStrMessage(), data.getnOverTime(), data.getnHandleTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getDtCreateTime(), data.getDtExecuteTime()
                , data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnParameterType(), data.getStrSourceURL(), data.getStrSourceType(), data.getStrDestinationURL(), data.getStrDestinationType(), data.getStrUseTime()
        ).intValue() != -1 ? true : false;
    }

    /**
     * 修改任务
     */
    @Transaction
    public boolean updateHttpHandOutPattern(HandOut data)
    {
        return DBQuery.query().executeUpdate("update tbHandOut set strName = ? , nState = ? , nConcurrencyNumber = ? , strMessage = ? , nOverTime = ? , nHandleTime = ? , nExecuteType = ? , nExecuteUnit = ? , dtCreateTime = ? , dtExecuteTime = ? , dtStartTime = ? , dtFinishedTime = ? , dtFailTime = ? , nParameterType = ? , strSourceURL = ? ,strSourceType = ? , strDestinationURL = ? , strDestinationType = ? , strUseTime = ? where lId = ?",
                data.getStrName(), data.getnState(), data.getnConcurrencyNumber(), data.getStrMessage(), data.getnOverTime(), data.getnHandleTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getDtCreateTime(), data.getDtExecuteTime()
                , data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnParameterType(), data.getStrSourceURL(), data.getStrSourceType(), data.getStrDestinationURL(), data.getStrDestinationType(), data.getStrUseTime(), data.getlId()
        )== 1 ? true : false;
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean updateDataServer(HandOut data)
    {
        String sql = "update tbHandOut set strName = ? , nConcurrencyNumber = ? , nOverTime = ? , nHandleTime = ? , nExecuteType = ? , nExecuteUnit = ? , dtExecuteTime = ? , nParameterType = ? , strSourceURL = ? ,strSourceType = ? , strDestinationURL = ? , strDestinationType = ? where lId = ? and nState = 5";
        return DBQuery.query().executeUpdate(sql, data.getStrName(), data.getnConcurrencyNumber(), data.getnOverTime(), data.getnHandleTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getDtExecuteTime()
                , data.getnParameterType(), data.getStrSourceURL(), data.getStrSourceType(), data.getStrDestinationURL(), data.getStrDestinationType(), data.getlId()) == 1 ? true : false;
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean startOrCloseTask(HandOut data, Integer state)
    {
        String sql = "update tbHandOut set nState = ? , strMessage = ? where lId = ? and nState = ?";
        int count = DBQuery.query().executeUpdate(sql, data.getnState(), data.getStrMessage(), data.getlId(), state);
        if (count == 0)
        {
            return false;
        }
        return true;
    }

    //删除本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean deleteTask(HandOut data)
    {
        String sql = "delete from tbHandOut  where lId = ? and nState = 5";
        return DBQuery.query().executeUpdate(sql, data.getlId()) == 1 ? true : false;
    }

    /**
     * 删除任务
     */
    @Transaction
    public boolean deleteHttpHandOutPattern(Integer id)
    {
        return DBQuery.query().executeUpdate("delete from tbHandOut where lId = ? ;", id) == 1 ? true : false;
    }

    /**
     * 查询任务列表(暂时查询所有的任务)
     */
    public List<HandOut> searchAllHttpHandOutPattern()
    {
        return DBQuery.query().query("select * from tbHandOut", new BeanListHandler<HandOut>(HandOut.class));
    }

    //查询HttpHandOutPattern通过id
    public HandOut queryById(Long id)
    {
        return DBQuery.query().query("select * from tbHandOut where lId = ?", new BeanHandler<HandOut>(HandOut.class), id);
    }

    /**
     * 查询本次的任务
     */
    public List<HandOut> searchHttpHandoutPatternWillSubmit()
    {
        return DBQuery.query().query("select * from tbHandOut where dtExecuteTime < now() and nState = 0", new BeanListHandler<HandOut>(HandOut.class));
    }

    //修改单个HttpHandOutPattern状态
    @Transaction
    public void writeError(Long id)
    {
        HandOut data = queryById(id);
        DBQuery.query().executeUpdate("update tbHandOut set nState = ? , strMessage = ? , dtStartTime = ? where lId = ?", 0, "未执行", null, data.getlId());
    }

    /**
     * 本地HttpHandOutPattern初始化（启动）
     * <p>
     * 作用：1.将与执行执行中完成的错误的Task初始化为未执行
     */
    @Transaction
    public void initHttpHandOutPattern()
    {
        DBQuery.query().executeUpdate("update tbHandOut set nState = 0 , strMessage = '预执行' where nState != 0 and nState != 5");
    }

    /**
     * 重置以失败的HttpHandOutPattern
     */
    @Transaction
    public void resetHttpHandOutPattern()
    {
        DBQuery.query().executeUpdate("update tbHandOut set nState = 0 , strMessage = '预执行' where nState = 4");
    }

}
