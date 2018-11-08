package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.data.Switch;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 信号量服务
 *
 * @author 付成垒
 */
@ServicePlugin
public class SwitchPlugin
{

    private static Logger logger = Logger.getLogger(SwitchPlugin.class);

    //初始化信号量
    @Transaction
    public boolean initSemaphore()
    {
        DBQuery.query().executeUpdate("update tbSwitch set strValue = 3 where lId != 4");
        DBQuery.query().executeUpdate("update tbSwitch set strValue = 0 where lId = 4");
        return true;
    }

    //获取所有信号量
    @Transaction
    public void obtainAllSemaphore()
    {
        List<Switch> datas = DBQuery.query().query("select * from tbSwitch for update", new BeanListHandler<Switch>(Switch.class));
        if (datas.size() == 0)
        {
            DBQuery.query().insert("insert into tbSwitch ( lId , strValue ) values ( 1 , 3 )");
            DBQuery.query().insert("insert into tbSwitch ( lId , strValue ) values ( 2 , 3 )");
            DBQuery.query().insert("insert into tbSwitch ( lId , strValue ) values ( 3 , 3 )");
            DBQuery.query().insert("insert into tbSwitch ( lId , strValue ) values ( 4 , 0 )");
        }
    }

    //信号量操作
    @Transaction
    public boolean semaphoreSwitch(String taskType, Integer state)
    {
        int id = this.getSemaphoreId(taskType);
        if (id == -10086)
            return false;
        Switch data = DBQuery.query().query("select strValue from tbSwitch where lId = ? for update", new BeanHandler<Switch>(Switch.class), id);
        if (data == null)
        {
            return false;
        }
        DBQuery.query().executeUpdate("update tbSwitch set strValue = ? where lId = ?", state.intValue(), id);
        return true;
    }

    //信号量批量操作
    @Transaction
    public boolean semaphoreSwitchAll(Integer state)
    {
        //此处无需做风险控制
        /*List<StateData> data = DBQuery.query().query("select state from state where id != 4 for update", new BeanListHandler<StateData>(StateData.class));
        if (data == null)
        {
            return false;
        }*/
        DBQuery.query().executeUpdate("update tbSwitch set strValue = ? where lId != 4", state.intValue());
        return true;
    }

    //信号量查询操作
    @Transaction
    public int showSemaphore(String taskType)
    {
        int id = this.getSemaphoreId(taskType);
        if (id == -10086)
            return -10086;
        Switch data = DBQuery.query().query("select strValue from tbSwitch where lId = ?", new BeanHandler<Switch>(Switch.class), id);
        return data.getStrValue();
    }

    //获取信号量
    @Transaction
    public boolean obtainSemaphore(String taskType)
    {
        int id = this.getSemaphoreId(taskType);
        if (id == -10086)
            return false;
        //该方案有风险
        /*StateData data = DBQuery.query().query("select state from state where id = ? and state = 0 for update", new BeanHandler<StateData>(StateData.class), id);
        if (data == null)
        {
            return false;
        }*/
        Switch data = DBQuery.query().query("select strValue from tbSwitch where lId = ? for update", new BeanHandler<Switch>(Switch.class), id);
        if (data == null || data.getStrValue().intValue() != 0)
        {
            return false;
        }
        DBQuery.query().executeUpdate("update tbSwitch set strValue = 1 where lId = ? and strValue = 0", id);
        return true;
    }

    //释放信号量
    @Transaction
    public boolean freeSemaphore(String taskType)
    {
        int id = this.getSemaphoreId(taskType);
        if (id == -10086)
            return false;
        DBQuery.query().executeUpdate("update tbSwitch set strValue = 0 where lId = ? and strValue = 1", id);
        return true;
    }

    //获取信号量id
    public int getSemaphoreId(String taskType)
    {
        int id = 0;
        if ("job".equals(taskType))
            id = 1;
        else if ("task".equals(taskType))
            id = 2;
        else if ("handOut".equals(taskType))
            id = 3;
        else if ("jobHandle".equals(taskType))
            id = 4;
        else
            return -10086;
        return id;
    }

}
