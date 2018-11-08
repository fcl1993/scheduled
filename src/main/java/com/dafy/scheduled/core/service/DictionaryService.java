package com.dafy.scheduled.core.service;

import com.dafy.scheduled.core.plugin.DictionaryPlugin;
import com.fuchenglei.core.container.Linked;
import com.fuchenglei.core.container.ReferencePlugin;

/**
 * 字典服务
 *
 * @author 付成垒
 */
@ReferencePlugin
public class DictionaryService
{

    @Linked
    private DictionaryPlugin dictionaryPlugin;

    //查询字典值
    public String searchDictionary(String key)
    {
        return dictionaryPlugin.searchDictionary(key);
    }

    //初始化安全码
    public boolean initSecurity()
    {
        return dictionaryPlugin.initSecurity();
    }

}
