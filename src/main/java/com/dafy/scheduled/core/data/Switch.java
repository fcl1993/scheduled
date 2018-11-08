package com.dafy.scheduled.core.data;

import com.fuchenglei.util.SerializableUtil;

/**
 * 任务信号量
 *
 * @author 付成垒
 */
public class Switch
{

    private Long lId;

    private Integer strValue;

    public Switch()
    {
    }

    public Switch(Long lId, Integer strValue)
    {
        this.lId = lId;
        this.strValue = strValue;
    }

    public Long getlId()
    {
        return lId;
    }

    public void setlId(Long lId)
    {
        this.lId = lId;
    }

    public Integer getStrValue()
    {
        return strValue;
    }

    public void setStrValue(Integer strValue)
    {
        this.strValue = strValue;
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof Switch))
            return false;
        if (lId != ((Switch) object).lId)
            return false;
        return true;
    }

    public String toString()
    {
        return new SerializableUtil().toJson(this);
    }

}
