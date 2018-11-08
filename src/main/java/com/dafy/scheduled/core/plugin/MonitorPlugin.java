package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.View;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.pool.HandOutThreadPool;
import com.dafy.scheduled.core.pool.ThreadPool;
import com.dafy.scheduled.core.data.Job;
import com.dafy.scheduled.core.data.Task;
import com.dafy.scheduled.core.util.Page;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ServicePlugin
public class MonitorPlugin
{

    private Logger logger = Logger.getLogger(MonitorPlugin.class);

    //获取当前task状态
    public HashMap<String, Object> getTaskState()
    {
        return ThreadPool.obtainTaskState();
    }

    //获取当前task状态
    public HashMap<String, Object> getTaskStateHandOut()
    {
        return HandOutThreadPool.obtainTaskState();
    }

    //获取localJob列表
    public View<List<Job>> getListJob(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        String sql = "select * from tbJob where 1 = 1";
        String countSQL = "select count( lId ) from tbJob where 1 = 1";
        List<Object> objects = new ArrayList<>();
        if (id != null)
        {
            sql += " and lId = ?";
            countSQL += " and lId = ?";
            objects.add(id);
        }
        if (state != null)
        {
            sql += " and nState = ?";
            countSQL += " and nState = ?";
            objects.add(state);
        }
        if (name != null && !"".equals(name))
        {
            sql += " and strName like ?";
            countSQL += " and strName like ?";
            objects.add("%" + name + "%");
        }
        long count = (long) DBQuery.query().query(countSQL, new ArrayHandler(), objects.toArray(new Object[objects.size()]))[0];
        sql += " order by lId limit ? , ? ";
        objects.add(Page.getLimit(currentPage, pageSize));
        objects.add(pageSize);
        List<Job> datas = DBQuery.query().query(sql, new BeanListHandler<Job>(Job.class), objects.toArray(new Object[objects.size()]));
        return new View<List<Job>>(count, pageSize, datas);
    }

    //获取localJob成功列表100调
    public View<List<Job>> getListJobSuccess(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        String sql = "select lId as lJobId , lJobId as lId , strName , strUrl , strType , nState , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime from tbJobSuccess where 1 = 1";
        String countSQL = "select count( lId ) from tbJobSuccess where 1 = 1";
        List<Object> objects = new ArrayList<>();
        if (id != null)
        {
            sql += " and lJobId = ?";
            countSQL += " and lJobId = ?";
            objects.add(id);
        }
        if (state != null)
        {
            sql += " and nState = ?";
            countSQL += " and nState = ?";
            objects.add(state);
        }
        if (name != null && !"".equals(name))
        {
            sql += " and strName like ?";
            countSQL += " and strName like ?";
            objects.add("%" + name + "%");
        }
        long count = (long) DBQuery.query().query(countSQL, new ArrayHandler(), objects.toArray(new Object[objects.size()]))[0];
        sql += " order by lJobId desc limit ? , ? ";
        objects.add(Page.getLimit(currentPage, pageSize));
        objects.add(pageSize);
        List<Job> datas = DBQuery.query().query(sql, new BeanListHandler<Job>(Job.class), objects.toArray(new Object[objects.size()]));
        return new View<List<Job>>(count, pageSize, datas);
    }

    //获取localJob失败列表100调
    public View<List<Job>> getListJobFail(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        String sql = "select lId as lJobId , lJobId as lId , strName , strUrl , strType , nState , strMessage , nTime , dtCreateTime , dtExecuteTime , dtStartTime , dtFinishedTime , dtFailTime , nOverTime , strArgs , strUseTime from tbJobFail where 1 = 1";
        String countSQL = "select count( lId ) from tbJobFail where 1 = 1";
        List<Object> objects = new ArrayList<>();
        if (id != null)
        {
            sql += " and lJobId = ?";
            countSQL += " and lJobId = ?";
            objects.add(id);
        }
        if (state != null)
        {
            sql += " and nState = ?";
            countSQL += " and nState = ?";
            objects.add(state);
        }
        if (name != null && !"".equals(name))
        {
            sql += " and strName like ?";
            countSQL += " and strName like ?";
            objects.add("%" + name + "%");
        }
        long count = (long) DBQuery.query().query(countSQL, new ArrayHandler(), objects.toArray(new Object[objects.size()]))[0];
        sql += " order by lJobId desc limit ? , ? ";
        objects.add(Page.getLimit(currentPage, pageSize));
        objects.add(pageSize);
        List<Job> datas = DBQuery.query().query(sql, new BeanListHandler<Job>(Job.class), objects.toArray(new Object[objects.size()]));
        return new View<List<Job>>(count, pageSize, datas);
    }

    //获取task列表
    public View<List<Task>> getListTask(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        String sql = "select * from tbTask where 1 = 1";
        String countSQL = "select count( lId ) from tbTask where 1 = 1";
        List<Object> objects = new ArrayList<>();
        if (id != null)
        {
            sql += " and lId = ?";
            countSQL += " and lId = ?";
            objects.add(id);
        }
        if (state != null)
        {
            sql += " and nState = ?";
            countSQL += " and nState = ?";
            objects.add(state);
        }
        if (name != null && !"".equals(name))
        {
            sql += " and strName like ?";
            countSQL += " and strName like ?";
            objects.add("%" + name + "%");
        }
        long count = (long) DBQuery.query().query(countSQL, new ArrayHandler(), objects.toArray(new Object[objects.size()]))[0];
        sql += " order by lId limit ? , ? ";
        objects.add(Page.getLimit(currentPage, pageSize));
        objects.add(pageSize);
        List<Task> datas = DBQuery.query().query(sql, new BeanListHandler<Task>(Task.class), objects.toArray(new Object[objects.size()]));
        return new View<List<Task>>(count, pageSize, datas);
    }

    //获取task列表
    public View<List<HandOut>> getHandOutListTask(Long id, Integer state, String name, Integer currentPage, Integer pageSize)
    {
        String sql = "select * from tbHandOut where 1 = 1";
        String countSQL = "select count( lId ) from tbHandOut where 1 = 1";
        List<Object> objects = new ArrayList<>();
        if (id != null)
        {
            sql += " and lId = ?";
            countSQL += " and lId = ?";
            objects.add(id);
        }
        if (state != null)
        {
            sql += " and nState = ?";
            countSQL += " and nState = ?";
            objects.add(state);
        }
        if (name != null && !"".equals(name))
        {
            sql += " and strName like ?";
            countSQL += " and strName like ?";
            objects.add("%" + name + "%");
        }
        long count = (long) DBQuery.query().query(countSQL, new ArrayHandler(), objects.toArray(new Object[objects.size()]))[0];
        sql += " order by lId limit ? , ? ";
        objects.add(Page.getLimit(currentPage, pageSize));
        objects.add(pageSize);
        List<HandOut> datas = DBQuery.query().query(sql, new BeanListHandler<HandOut>(HandOut.class), objects.toArray(new Object[objects.size()]));
        return new View<List<HandOut>>(count, pageSize, datas);
    }

}
