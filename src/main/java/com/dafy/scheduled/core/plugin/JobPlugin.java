package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.data.Job;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 本地job服务
 *
 * @author 付成垒
 */
@ServicePlugin
public class JobPlugin
{

    private static Logger logger = Logger.getLogger(JobPlugin.class);

    //新增本地调用任务
    @Transaction
    public boolean insertData(Job data)
    {
        String sql = "insert into tbJob ( strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        return DBQuery.query().insert(sql, data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getnTime(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getStrArgs(), data.getStrUseTime()).intValue() != -1 ? true : false;
    }

    //修改本地调用任务
    @Transaction
    public boolean updateData(Job data)
    {
        String sql = "update tbJob set strName = ? , nState = ? , strUrl = ? , strType = ? , strMessage = ? , nTime = ? ,dtCreateTime = ? , dtExecuteTime = ? , dtStartTime = ? , dtFinishedTime = ? , dtFailTime = ? , nOverTime = ? , strArgs = ? , strUseTime = ? where lId=?";
        int count = DBQuery.query().executeUpdate(sql, data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getnTime(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getStrArgs(), data.getStrUseTime(), data.getlId());
        if (count == 0)
        {
            return false;
        }
        return true;
    }

    //删除本地调用任务
    @Transaction
    public boolean deleteData(Long id)
    {
        int count = DBQuery.query().executeUpdate("delete from tbJob where lId = ?", id);
        if (count == 0)
        {
            return false;
        }
        return true;
    }

    //查询localJob通过id
    public Job queryById(Long id)
    {
        return DBQuery.query().query("select * from tbJob where lId = ?", new BeanHandler<Job>(Job.class), id);
    }

    //查询失败localJob通过id
    public Job queryFailById(Long id)
    {
        return DBQuery.query().query("select lJobId as lId , strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime from tbJobFail where lJobId = ?", new BeanHandler<Job>(Job.class), id);
    }

    //查询成功localJob通过id
    public Job querySuccessById(Long id)
    {
        return DBQuery.query().query("select  lJobId as lId , strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime from tbJobSuccess where lJobId = ?", new BeanHandler<Job>(Job.class), id);
    }

    //查询需要执行的job添加到执行队列
    @Transaction
    public List<Job> querySoonExecuteLocalJob()
    {
        return DBQuery.query().query("select * from tbJob where dtExecuteTime < now() and nState=0 order by lId limit 500", new BeanListHandler<Job>(Job.class));
    }

    /**
     * 本地job初始化（启动）
     * <p>
     * 作用：1.将与执行的job初始化为未执行
     */
    @Transaction
    public void initJob()
    {
        DBQuery.query().executeUpdate("update tbJob set nState=0 , strMessage='预执行' where nState = 1");
    }

    /**
     * 获取job通过state
     */
    public List<Job> obtainLocalJobByState(Integer state)
    {
        return DBQuery.query().query("select * from tbJob where nState = ? order by lId limit 500", new BeanListHandler<Job>(Job.class), state);
    }

    //写入数据到记录
    @Transaction
    public void writeData(Job data, String table)
    {
        /*String sql = "";
        if ("tbJobFail".equals(table))
            sql = "insert into tbJobFail ( jobId , strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        if ("tbJobSuccess".equals(table))
            sql = "insert into tbJobSuccess ( jobId , strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";*/
        String sql = "insert into " + table+ " ( lJobId , strName , nState , strUrl , strType , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
        DBQuery.query().insert(sql, data.getlId(), data.getStrName(), data.getnState(), data.getStrUrl(), data.getStrType(), data.getStrMessage(), data.getnTime(), data.getDtCreateTime(), data.getDtExecuteTime(), data.getDtStartTime(), data.getDtFinishedTime(), data.getDtFailTime(), data.getnOverTime(), data.getStrArgs(), data.getStrUseTime());
        DBQuery.query().executeUpdate("delete from tbJob where lId = ?", data.getlId());
    }

}
