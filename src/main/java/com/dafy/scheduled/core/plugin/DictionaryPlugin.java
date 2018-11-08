package com.dafy.scheduled.core.plugin;

import com.dafy.scheduled.core.data.Dictionary;
import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.util.UUID;

/**
 * 字典操作
 *
 * @author 付成垒
 */
@ServicePlugin
public class DictionaryPlugin
{

    //查询字典
    public String searchDictionary(String key)
    {
        if (key == null || "".equals(key)) return null;
        Dictionary data = DBQuery.query().query("select * from tbDictionary where strKey = ?", new BeanHandler<Dictionary>(Dictionary.class), key);
        if (data == null) return null;
        return data.getStrValue();
    }

    //安全码初始化
    @Transaction
    public boolean initSecurity()
    {
        Dictionary security = DBQuery.query().query("select * from tbDictionary where strKey = 'security'", new BeanHandler<Dictionary>(Dictionary.class));
        if (security == null)
        {
            DBQuery.query().executeUpdate("insert into tbDictionary ( strKey , strValue ) values ( 'security' , ? )", UUID.randomUUID().toString().replaceAll("-", ""));
        }
        else
        {
            DBQuery.query().executeUpdate("update tbDictionary set strValue = ? where strKey = 'security'", UUID.randomUUID().toString().replaceAll("-", ""));
        }
        Dictionary innerSecurity = DBQuery.query().query("select * from tbDictionary where strKey = 'innerSecurity'", new BeanHandler<Dictionary>(Dictionary.class));
        if (innerSecurity == null)
        {
            DBQuery.query().executeUpdate("insert into tbDictionary ( strKey , strValue ) values ( 'innerSecurity' , ? )", UUID.randomUUID().toString().replaceAll("-", ""));
        }
        return true;
    }

}
