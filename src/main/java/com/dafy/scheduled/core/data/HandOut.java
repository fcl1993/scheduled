package com.dafy.scheduled.core.data;

import com.fuchenglei.util.SerializableUtil;

import java.io.Serializable;

/**
 * 分发模式
 *
 * @author 付成垒
 */
public class HandOut implements Serializable
{

    private static final long serialVersionUID = 1074077904222675800L;

    //任务id
    private Long lId;

    //任务名称
    private String strName;

    //任务当前执行状态
    private Integer nState;

    //并发任务数
    private Integer nConcurrencyNumber;

    //任务执行描述信息
    private String strMessage;

    //任务执行单词数据处理超时时间默认为5秒
    private Integer nOverTime;

    //任务单次处理超时时间
    private Integer nHandleTime;

    //任务调用频率(单位类型)
    private Integer nExecuteType;

    //任务调用频率(单位数)
    private Integer nExecuteUnit;

    //任务创建时间
    private String dtCreateTime;

    //任务下一次执行时间
    private String dtExecuteTime;

    //任务最近一次开始时间
    private String dtStartTime;

    //任务最后一次完成时间
    private String dtFinishedTime;

    //任务最后一次失败时间
    private String dtFailTime;

    //参数模式 0:JSON模式 1:MAP模式
    private Integer nParameterType;

    //任务数据源链接
    private String strSourceURL;

    //任务数据源请求方式
    private String strSourceType;

    //任务请求目标地址
    private String strDestinationURL;

    //任务目标地址请求方式
    private String strDestinationType;

    //调用耗时
    private String strUseTime;

    public HandOut()
    {
    }

    public HandOut(Long lId, String strName, Integer nState, Integer nConcurrencyNumber, String strMessage, Integer nOverTime, Integer nHandleTime, Integer nExecuteType, Integer nExecuteUnit, String dtCreateTime, String dtExecuteTime, String dtStartTime, String dtFinishedTime, String dtFailTime, Integer nParameterType, String strSourceURL, String strSourceType, String strDestinationURL, String strDestinationType, String strUseTime)
    {
        this.lId = lId;
        this.strName = strName;
        this.nState = nState;
        this.nConcurrencyNumber = nConcurrencyNumber;
        this.strMessage = strMessage;
        this.nOverTime = nOverTime;
        this.nHandleTime = nHandleTime;
        this.nExecuteType = nExecuteType;
        this.nExecuteUnit = nExecuteUnit;
        this.dtCreateTime = dtCreateTime;
        this.dtExecuteTime = dtExecuteTime;
        this.dtStartTime = dtStartTime;
        this.dtFinishedTime = dtFinishedTime;
        this.dtFailTime = dtFailTime;
        this.nParameterType = nParameterType;
        this.strSourceURL = strSourceURL;
        this.strSourceType = strSourceType;
        this.strDestinationURL = strDestinationURL;
        this.strDestinationType = strDestinationType;
        this.strUseTime = strUseTime;
    }

    public Long getlId()
    {
        return lId;
    }

    public void setlId(Long lId)
    {
        this.lId = lId;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    public Integer getnState()
    {
        return nState;
    }

    public void setnState(Integer nState)
    {
        this.nState = nState;
    }

    public Integer getnConcurrencyNumber()
    {
        return nConcurrencyNumber;
    }

    public void setnConcurrencyNumber(Integer nConcurrencyNumber)
    {
        this.nConcurrencyNumber = nConcurrencyNumber;
    }

    public String getStrMessage()
    {
        return strMessage;
    }

    public void setStrMessage(String strMessage)
    {
        this.strMessage = strMessage;
    }

    public Integer getnOverTime()
    {
        return nOverTime;
    }

    public void setnOverTime(Integer nOverTime)
    {
        this.nOverTime = nOverTime;
    }

    public Integer getnHandleTime()
    {
        return nHandleTime;
    }

    public void setnHandleTime(Integer nHandleTime)
    {
        this.nHandleTime = nHandleTime;
    }

    public Integer getnExecuteType()
    {
        return nExecuteType;
    }

    public void setnExecuteType(Integer nExecuteType)
    {
        this.nExecuteType = nExecuteType;
    }

    public Integer getnExecuteUnit()
    {
        return nExecuteUnit;
    }

    public void setnExecuteUnit(Integer nExecuteUnit)
    {
        this.nExecuteUnit = nExecuteUnit;
    }

    public String getDtCreateTime()
    {
        return dtCreateTime;
    }

    public void setDtCreateTime(String dtCreateTime)
    {
        this.dtCreateTime = dtCreateTime;
    }

    public String getDtExecuteTime()
    {
        return dtExecuteTime;
    }

    public void setDtExecuteTime(String dtExecuteTime)
    {
        this.dtExecuteTime = dtExecuteTime;
    }

    public String getDtStartTime()
    {
        return dtStartTime;
    }

    public void setDtStartTime(String dtStartTime)
    {
        this.dtStartTime = dtStartTime;
    }

    public String getDtFinishedTime()
    {
        return dtFinishedTime;
    }

    public void setDtFinishedTime(String dtFinishedTime)
    {
        this.dtFinishedTime = dtFinishedTime;
    }

    public String getDtFailTime()
    {
        return dtFailTime;
    }

    public void setDtFailTime(String dtFailTime)
    {
        this.dtFailTime = dtFailTime;
    }

    public Integer getnParameterType()
    {
        return nParameterType;
    }

    public void setnParameterType(Integer nParameterType)
    {
        this.nParameterType = nParameterType;
    }

    public String getStrSourceURL()
    {
        return strSourceURL;
    }

    public void setStrSourceURL(String strSourceURL)
    {
        this.strSourceURL = strSourceURL;
    }

    public String getStrSourceType()
    {
        return strSourceType;
    }

    public void setStrSourceType(String strSourceType)
    {
        this.strSourceType = strSourceType;
    }

    public String getStrDestinationURL()
    {
        return strDestinationURL;
    }

    public void setStrDestinationURL(String strDestinationURL)
    {
        this.strDestinationURL = strDestinationURL;
    }

    public String getStrDestinationType()
    {
        return strDestinationType;
    }

    public void setStrDestinationType(String strDestinationType)
    {
        this.strDestinationType = strDestinationType;
    }

    public String getStrUseTime()
    {
        return strUseTime;
    }

    public void setStrUseTime(String strUseTime)
    {
        this.strUseTime = strUseTime;
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof HandOut))
            return false;
        if (lId != ((HandOut) object).lId)
            return false;
        return true;
    }

    public String toString()
    {
        return new SerializableUtil().toJson(this);
    }

}
