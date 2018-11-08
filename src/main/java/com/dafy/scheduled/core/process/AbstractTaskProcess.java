package com.dafy.scheduled.core.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * task
 *
 * @author 付成垒
 */
public abstract class AbstractTaskProcess
{

    public static final ResourceBundle scheduled = ResourceBundle.getBundle("scheduled");

    //job运行状态-未执行
    public static final Integer TASK_STATE_BEFORE = 0;

    //job运行状态-预执行
    public static final Integer TASK_STATE_PRE_EXECUTION = 1;

    //job运行状态-执行中
    public static final Integer TASK_STATE_RUNNING = 2;

    //job运行状态-已完成
    public static final Integer TASK_STATE_COMPLETE = 3;

    //job运行状态-已失败
    public static final Integer TASK_STATE_EXCEPTION = 4;

    //job运行状态-暂停中
    public static final Integer TASK_STATE_PAUSE = 5;

    //秒
    public static final Integer TASK_SECOND = 1;

    //分
    public static final Integer TASK_MINUTE = 2;

    //时
    public static final Integer TASK_HOUR = 3;

    //天
    public static final Integer TASK_DAY = 4;

    //周
    public static final Integer TASK_WEEK = 5;

    //月
    public static final Integer TASK_MONTH = 6;

    //年
    public static final Integer TASK_YEAR = 7;

    Integer state;

    Boolean type;   //true为正常执行 false为立即执行

    public abstract void executor();

    public AbstractTaskProcess()
    {
        this.state = TASK_STATE_BEFORE;
    }

    /**
     * 下次调用时间计算
     */
    public static String computingTime(String executeDate, int executeType, int executeUnit)
    {
        Date date = null;
        long thisExecuteTime = 0;
        String nowDate = null;
        long now = 0;
        Date nowTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(nowTime);
        try
        {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(executeDate);
            thisExecuteTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(executeDate).getTime();
            nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowTime);
            now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(nowDate).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (executeType == TASK_SECOND)  //秒
        {
            int time = (int) ((now - thisExecuteTime) / 1000 / executeUnit);
            calendar.add(Calendar.SECOND, executeUnit * (time + 1));
        }
        if (executeType == TASK_MINUTE)  //分
        {
            int time = (int) ((now - thisExecuteTime) / 1000 / 60 / executeUnit);
            calendar.add(Calendar.MINUTE, executeUnit * (time + 1));
        }
        if (executeType == TASK_HOUR)  //时
        {
            int time = (int) ((now - thisExecuteTime) / 1000 / 60 / 60 / executeUnit);
            calendar.add(Calendar.HOUR_OF_DAY, executeUnit * (time + 1));
        }
        if (executeType == TASK_DAY)  //天
        {
            int time = (int) ((now - thisExecuteTime) / 1000 / 60 / 60 / 24 / executeUnit);
            calendar.add(Calendar.DAY_OF_WEEK, executeUnit * (time + 1));
        }
        if (executeType == TASK_WEEK)  //周
        {
            int time = (int) ((now - thisExecuteTime) / 1000 / 60 / 60 / 24 / 7 / executeUnit);
            calendar.add(Calendar.WEEK_OF_MONTH, executeUnit * (time + 1));
        }
        if (executeType == TASK_MONTH)  //月
        {
            int time = ((c.get(Calendar.MONTH) - calendar.get(Calendar.MONTH)) + 12 * (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR))) / executeUnit;
            calendar.add(Calendar.MONTH, executeUnit * (time + 1));
        }
        if (executeType == TASK_YEAR)  //年
        {
            int time = (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR)) / executeUnit;
            calendar.add(Calendar.YEAR, executeUnit * time + 1);
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    /**
     * 计算耗时
     */
    public String computingUseTime(long startTime, long endTime)
    {
        long time = endTime - startTime;
        if (time < 86400000)
        {
            long milliseconds = time % 1000;
            if (time < 1000)
                return milliseconds + "毫秒";
            long seconds = ((time - milliseconds) / 1000) % 60;
            if (time < 60000)
                return seconds + "秒" + milliseconds + "毫秒";
            long minutes = ((time - seconds * 1000 - milliseconds) / 1000 / 60) % 60;
            if (time < 3600000)
                return minutes + "分" + seconds + "秒" + milliseconds + "毫秒";
            long hours = (time - minutes * 60 * 1000 - seconds * 1000 - milliseconds) / 60 / 60 / 1000 % 24;
            return hours + "时" + minutes + "分" + seconds + "秒" + milliseconds + "毫秒";
        }
        else
            return "执行时间过长,超过一天";
    }

}
