package com.dafy.scheduled.core.scheme;

import com.fuchenglei.db.core.ServicePlugin;
import com.fuchenglei.db.core.Transaction;
import com.fuchenglei.db.operate.DBQuery;
import com.fuchenglei.util.KeyAndValue;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 数据库数据初始化状态
 *
 * @author 付成垒
 */
@ServicePlugin
public class DataCenter
{

    private static Logger logger = Logger.getLogger(DataCenter.class);

    /**
     * 初始化数据库
     */
    @Transaction
    public void tableInit()
    {
        List<KeyAndValue> data = new ArrayList<KeyAndValue>();
        int i = 0;
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbJob';", "CREATE TABLE `tbJob` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
                "  `strName` varchar(500) NOT NULL COMMENT '任务名称',\n" +
                "  `strUrl` varchar(255) NOT NULL COMMENT '请求的url',\n" +
                "  `strType` varchar(4) NOT NULL COMMENT '请求的类型',\n" +
                "  `nState` int(1) NOT NULL COMMENT '任务执行状态0:未执行 1:预执行 2:执行中 3:执行完成 4:执行失败',\n" +
                "  `strMessage` varchar(100) DEFAULT NULL COMMENT '执行消息',\n" +
                "  `nTime` int(11) DEFAULT NULL COMMENT '延时时间',\n" +
                "  `dtCreateTime` datetime NOT NULL COMMENT 'job创建时间',\n" +
                "  `dtExecuteTime` datetime NOT NULL COMMENT 'job执行时间',\n" +
                "  `dtStartTime` datetime DEFAULT NULL COMMENT 'job开始时间',\n" +
                "  `dtFinishedTime` datetime DEFAULT NULL COMMENT 'job完成时间',\n" +
                "  `dtFailTime` datetime DEFAULT NULL COMMENT 'job失败时间',\n" +
                "  `nOverTime` int(5) DEFAULT NULL COMMENT '超时时间',\n" +
                "  `strArgs` varchar(500) NOT NULL COMMENT 'job执行参数',\n" +
                "  `strUseTime` varchar(50) DEFAULT NULL COMMENT '调用耗时',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbTask';", "CREATE TABLE `tbTask` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
                "  `strName` varchar(500) NOT NULL COMMENT '任务名称',\n" +
                "  `strUrl` varchar(255) NOT NULL COMMENT '请求的url',\n" +
                "  `strType` varchar(4) NOT NULL COMMENT '请求类型',\n" +
                "  `nState` int(1) DEFAULT NULL COMMENT '任务执行状态0:未执行 1:预执行 2:执行中 3:执行完成 4:执行失败 5:暂停执行',\n" +
                "  `strMessage` varchar(100) DEFAULT NULL COMMENT '执行消息',\n" +
                "  `dtCreateTime` datetime NOT NULL COMMENT 'task创建时间',\n" +
                "  `dtExecuteTime` datetime NOT NULL COMMENT 'task执行时间',\n" +
                "  `dtStartTime` datetime DEFAULT NULL COMMENT 'task开始时间',\n" +
                "  `dtFinishedTime` datetime DEFAULT NULL COMMENT 'task完成时间',\n" +
                "  `dtFailTime` datetime DEFAULT NULL COMMENT 'task失败时间',\n" +
                "  `nOverTime` bigint(5) DEFAULT NULL COMMENT '超时时间',\n" +
                "  `nExecuteType` int(1) DEFAULT NULL COMMENT '循环类型1:秒 2:分 3:时 4:天 5:周 6:月 7:年',\n" +
                "  `nExecuteUnit` int(11) DEFAULT NULL COMMENT '间隔单元',\n" +
                "  `strArgs` varchar(500) NOT NULL COMMENT 'task执行参数',\n" +
                "  `strUseTime` varchar(50) DEFAULT NULL COMMENT '调用耗时',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbJobFail';", "CREATE TABLE `tbJobFail` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
                "  `lJobId` int(11) NOT NULL COMMENT 'httpJobId',\n" +
                "  `strName` varchar(500) NOT NULL COMMENT '任务名称',\n" +
                "  `strUrl` varchar(255) NOT NULL COMMENT '请求的url',\n" +
                "  `strType` varchar(4) NOT NULL COMMENT '请求的类型',\n" +
                "  `nState` int(1) NOT NULL COMMENT '任务执行状态0:未执行 1:预执行 2:执行中 3:执行完成 4:执行失败',\n" +
                "  `strMessage` varchar(100) DEFAULT NULL COMMENT '执行消息',\n" +
                "  `nTime` int(11) DEFAULT NULL COMMENT '延时时间',\n" +
                "  `dtCreateTime` datetime NOT NULL COMMENT 'job创建时间',\n" +
                "  `dtExecuteTime` datetime NOT NULL COMMENT 'job执行时间',\n" +
                "  `dtStartTime` datetime DEFAULT NULL COMMENT 'job开始时间',\n" +
                "  `dtFinishedTime` datetime DEFAULT NULL COMMENT 'job完成时间',\n" +
                "  `dtFailTime` datetime DEFAULT NULL COMMENT 'job失败时间',\n" +
                "  `nOverTime` int(5) DEFAULT NULL COMMENT '超时时间',\n" +
                "  `strArgs` varchar(500) NOT NULL COMMENT 'job执行参数',\n" +
                "  `strUseTime` varchar(50) DEFAULT NULL COMMENT '调用耗时',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbJobSuccess';", "CREATE TABLE `tbJobSuccess` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
                "  `lJobId` int(11) NOT NULL COMMENT 'httpJobId',\n" +
                "  `strName` varchar(500) NOT NULL COMMENT '任务名称',\n" +
                "  `strUrl` varchar(255) NOT NULL COMMENT '请求的url',\n" +
                "  `strType` varchar(4) NOT NULL COMMENT '请求的类型',\n" +
                "  `nState` int(1) NOT NULL COMMENT '任务执行状态0:未执行 1:预执行 2:执行中 3:执行完成 4:执行失败',\n" +
                "  `strMessage` varchar(100) DEFAULT NULL COMMENT '执行消息',\n" +
                "  `nTime` int(11) DEFAULT NULL COMMENT '延时时间',\n" +
                "  `dtCreateTime` datetime NOT NULL COMMENT 'job创建时间',\n" +
                "  `dtExecuteTime` datetime NOT NULL COMMENT 'job执行时间',\n" +
                "  `dtStartTime` datetime DEFAULT NULL COMMENT 'job开始时间',\n" +
                "  `dtFinishedTime` datetime DEFAULT NULL COMMENT 'job完成时间',\n" +
                "  `dtFailTime` datetime DEFAULT NULL COMMENT 'job失败时间',\n" +
                "  `nOverTime` int(5) DEFAULT NULL COMMENT '超时时间',\n" +
                "  `strArgs` varchar(500) NOT NULL COMMENT 'job执行参数',\n" +
                "  `strUseTime` varchar(50) DEFAULT NULL COMMENT '调用耗时',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbHandOut';", "CREATE TABLE `tbHandOut` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',\n" +
                "  `strName` varchar(100) DEFAULT NULL COMMENT '任务名称',\n" +
                "  `nState` int(1) DEFAULT NULL COMMENT '任务当前执行状态0:未执行 1:预执行 2:执行中 3:执行完成 4:执行失败 5:暂停执行',\n" +
                "  `nConcurrencyNumber` int(5) DEFAULT NULL COMMENT '并发数',\n" +
                "  `strMessage` varchar(255) DEFAULT NULL COMMENT '任务执行描述信息',\n" +
                "  `nOverTime` int(11) DEFAULT NULL COMMENT '任务执行单词数据处理超时时间默认为5秒',\n" +
                "  `nHandleTime` int(11) DEFAULT NULL COMMENT '单词数据处理超时时间默认为5秒',\n" +
                "  `nExecuteType` int(1) DEFAULT NULL COMMENT '任务调用频率(单位类型)1:秒 2:分 3:时 4:天 5:周 6:月 7:年',\n" +
                "  `nExecuteUnit` int(11) DEFAULT NULL COMMENT '任务调用频率(单位数)',\n" +
                "  `dtCreateTime` datetime DEFAULT NULL COMMENT '任务创建时间',\n" +
                "  `dtExecuteTime` datetime DEFAULT NULL COMMENT '任务下一次执行时间',\n" +
                "  `dtStartTime` datetime DEFAULT NULL COMMENT '任务最近一次开始时间',\n" +
                "  `dtFinishedTime` datetime DEFAULT NULL COMMENT '任务最后一次完成时间',\n" +
                "  `dtFailTime` datetime DEFAULT NULL COMMENT '任务最后一次失败时间',\n" +
                "  `nParameterType` int(1) DEFAULT NULL COMMENT '参数模式 0:JSON模式 1:MAP模式',\n" +
                "  `strSourceURL` varchar(255) DEFAULT NULL COMMENT '任务数据源链接',\n" +
                "  `strSourceType` varchar(5) DEFAULT NULL COMMENT '任务数据源请求方式',\n" +
                "  `strDestinationURL` varchar(255) DEFAULT NULL COMMENT '任务请求目标地址',\n" +
                "  `strDestinationType` varchar(5) DEFAULT NULL COMMENT '任务目标地址请求方式',\n" +
                "  `strUseTime` varchar(50) DEFAULT NULL COMMENT '调用耗时',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbSwitch';", "CREATE TABLE `tbSwitch` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT '1:JOB 2:TASK 3:分发 4:JOB回收',\n" +
                "  `strValue` int(1) NOT NULL DEFAULT '0' COMMENT '0:空闲 1:启用 3:停止中',\n" +
                "  PRIMARY KEY (`lId`),\n" +
                "  KEY `状态` (`strValue`) USING BTREE COMMENT '状态'\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;"));
        data.add(new KeyAndValue("SELECT table_name FROM information_schema.TABLES WHERE table_name ='tbDictionary';", "CREATE TABLE `tbDictionary` (\n" +
                "  `lId` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `strKey` varchar(50) DEFAULT NULL COMMENT 'key',\n" +
                "  `strValue` varchar(255) DEFAULT NULL COMMENT 'value',\n" +
                "  PRIMARY KEY (`lId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"));
        i = data.size();
        for (KeyAndValue arg : data)
        {
            Object[] objects = DBQuery.query().query(arg.getKey(), new ArrayHandler());
            if (objects.length == 0)
            {
                DBQuery.query().executeUpdate(arg.getValue());
            }
            i--;
        }
        if (0 != i)
            logger.info("数据库初始化完成..");
    }

}
