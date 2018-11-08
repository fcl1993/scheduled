package com.dafy.scheduled.core.data;

import com.fuchenglei.util.SerializableUtil;

/**
 * 字典
 *
 * @author 付成垒
 */
public class Dictionary
{

    private Long id;

    private String strKey;

    private String strValue;

    public Dictionary()
    {
    }

    public Dictionary(Long id, String strKey, String strValue)
    {
        this.id = id;
        this.strKey = strKey;
        this.strValue = strValue;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getStrKey()
    {
        return strKey;
    }

    public void setStrKey(String strKey)
    {
        this.strKey = strKey;
    }

    public String getStrValue()
    {
        return strValue;
    }

    public void setStrValue(String strValue)
    {
        this.strValue = strValue;
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof Dictionary))
            return false;
        if (id != ((Dictionary) object).id)
            return false;
        return true;
    }

    public String toString()
    {
        return new SerializableUtil().toJson(this);
    }

}
