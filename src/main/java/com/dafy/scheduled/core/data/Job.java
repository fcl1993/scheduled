package com.dafy.scheduled.core.data;

import com.fuchenglei.util.SerializableUtil;

import java.io.Serializable;

/**
 * http job
 *
 * @author 付成垒
 */
public class Job implements Serializable
{

    private static final long serialVersionUID = 1518413590464300925L;

    //数据id
    private Long lId;

    //任务名称
    private String strName;

    //调用的url
    private String strUrl;

    //调用的方式
    private String strType;

    //job的状态
    private Integer nState;

    //job执行消息
    private String strMessage;

    //job延时时间
    private Integer nTime;

    //job创建时间
    private String dtCreateTime;

    //job执行时间
    private String dtExecuteTime;

    //job开始时间
    private String dtStartTime;

    //job最后一次完成时间
    private String dtFinishedTime;

    //job失败时间
    private String dtFailTime;

    //超时时间
    private Integer nOverTime;

    //job调用参数
    private String strArgs;

    //调用耗时
    private String strUseTime;

    public Job()
    {
    }

    public Job(Long lId, String strName, String strUrl, String strType, Integer nState, String strMessage, Integer nTime, String dtCreateTime, String dtExecuteTime, String dtStartTime, String dtFinishedTime, String dtFailTime, Integer nOverTime, String strArgs, String strUseTime)
    {
        this.lId = lId;
        this.strName = strName;
        this.strUrl = strUrl;
        this.strType = strType;
        this.nState = nState;
        this.strMessage = strMessage;
        this.nTime = nTime;
        this.dtCreateTime = dtCreateTime;
        this.dtExecuteTime = dtExecuteTime;
        this.dtStartTime = dtStartTime;
        this.dtFinishedTime = dtFinishedTime;
        this.dtFailTime = dtFailTime;
        this.nOverTime = nOverTime;
        this.strArgs = strArgs;
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

    public String getStrUrl()
    {
        return strUrl;
    }

    public void setStrUrl(String strUrl)
    {
        this.strUrl = strUrl;
    }

    public String getStrType()
    {
        return strType;
    }

    public void setStrType(String strType)
    {
        this.strType = strType;
    }

    public Integer getnState()
    {
        return nState;
    }

    public void setnState(Integer nState)
    {
        this.nState = nState;
    }

    public String getStrMessage()
    {
        return strMessage;
    }

    public void setStrMessage(String strMessage)
    {
        this.strMessage = strMessage;
    }

    public Integer getnTime()
    {
        return nTime;
    }

    public void setnTime(Integer nTime)
    {
        this.nTime = nTime;
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

    public Integer getnOverTime()
    {
        return nOverTime;
    }

    public void setnOverTime(Integer nOverTime)
    {
        this.nOverTime = nOverTime;
    }

    public String getStrArgs()
    {
        return strArgs;
    }

    public void setStrArgs(String strArgs)
    {
        this.strArgs = strArgs;
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
        if (!(object instanceof Job))
            return false;
        if (lId != ((Job) object).lId)
            return false;
        return true;
    }

    public String toString()
    {
        return new SerializableUtil().toJson(this);
    }

}
