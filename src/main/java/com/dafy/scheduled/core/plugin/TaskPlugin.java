package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.data.Task;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 本地任务
 *
 * @author 付成垒
 */
@ServicePlugin
public class TaskPlugin
{

    private static Logger logger = Logger.getLogger(TaskPlugin.class);

    //新增本地调用任务Task
    @Transaction
    public boolean insertData(Task data)
    {
        String sql = "insert into tbTask ( strName , nState , strUrl , strType , strMessage , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , nExecuteType , nExecuteUnit , strArgs , strUseTime ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        return DBQuery.query().insert(sql, data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getStrArgs(), data.getStrUseTime()).intValue() != -1 ? true : false;
    }

    //修改本地调用任务Task
    @Transaction
    public boolean updateData(Task data)
    {
        String sql = "update tbTask set strName = ? , nState = ? , strUrl = ? , strType = ? , strMessage = ? , dtCreateTime = ? , dtExecuteTime = ? , dtStartTime = ? , dtFinishedTime = ? , dtFailTime = ? , nOverTime = ? , nExecuteType = ? , nExecuteUnit = ? , strArgs = ? , strUseTime = ? where lId = ?";
        return DBQuery.query().executeUpdate(sql, data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getStrArgs(), data.getStrUseTime(), data.getlId()) == 1 ? true : false;
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean updateDataServer(Task data)
    {
        String sql = "update tbTask set strName = ? , nState = ? , strUrl = ? , strType = ? , strMessage = ? , dtCreateTime = ? , dtExecuteTime = ? , dtStartTime = ? , dtFinishedTime = ? , dtFailTime = ? , nOverTime = ? , nExecuteType = ? , nExecuteUnit = ? , strArgs = ? , strUseTime = ? where lId = ? and nState = 5";
        return DBQuery.query().executeUpdate(sql, data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getnExecuteType(), data.getnExecuteUnit(), data.getStrArgs(), data.getStrUseTime(), data.getlId()) == 1 ? true : false;
    }

    //修改本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean startOrCloseTask(Task data, Integer state)
    {
        String sql = "update tbTask set nState = ? , strMessage = ? where lId = ? and nState = ?";
        return DBQuery.query().executeUpdate(sql, data.getnState(), data.getStrMessage(), data.getlId(), state) == 1 ? true : false;
    }

    //删除本地调用任务Task  //只有在任务被停止时才能修改任务
    @Transaction
    public boolean deleteTask(Task data)
    {
        String sql = "delete from tbTask  where lId = ? and nState = 5";
        return DBQuery.query().executeUpdate(sql, data.getlId()) == 1 ? true : false;
    }

    //删除本地调用任务Task
    @Transaction
    public boolean deleteData(Long id)
    {
        return DBQuery.query().executeUpdate("delete from tbTask where lId = ?", id) == 1 ? true : false;
    }

    //查询localTask通过id
    public Task queryById(Long id)
    {
        return DBQuery.query().query("select * from tbTask where lId = ?", new BeanHandler<Task>(Task.class), id);
    }

    //查询需要执行的Task添加到执行队列
    public List<Task> querySoonExecuteLocalTask()
    {
        return DBQuery.query().query("select * from tbTask where dtExecuteTime < now() and nState = 0", new BeanListHandler<Task>(Task.class));
    }

    //修改单个Task状态
    @Transaction
    public void writeError(Long id)
    {
        Task data = queryById(id);
        DBQuery.query().executeUpdate("update tbTask set nState = ? , strMessage = ? , dtStartTime = ? where lId = ?", 0, "未执行", null, data.getlId());
    }

    /**
     * 本地Task初始化（启动）
     * <p>
     * 作用：1.将与执行执行中完成的错误的Task初始化为未执行
     */
    @Transaction
    public void initTask()
    {
        DBQuery.query().executeUpdate("update tbTask set nState = 0 , strMessage = '预执行' where nState != 0 and nState != 5");
    }

    /**
     * 重置以失败的task
     */
    @Transaction
    public void resetHttpTask()
    {
        DBQuery.query().executeUpdate("update tbTask set nState = 0 , strMessage = '预执行' where nState = 4");
    }

}