package com.dafy.scheduled.core.proxy;

import com.dafy.scheduled.core.process.ProcessResult;
import com.fuchenglei.util.SerializableUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成代理
 *
 * @author 付成垒
 */
public class ProxyInstance<T> implements InvocationHandler
{

    private static Logger logger = Logger.getLogger(ProxyInstance.class);

    private T object;

    public T newInstance(T target)
    {
        this.object = target;
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this
        );
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        long before = System.currentTimeMillis();
        Object object = null;
        object = method.invoke(this.object, args);
        Field field = ((ProxyInstance) this).object.getClass().getDeclaredField("metaData");
        field.setAccessible(true);
        ProcessResult result = ProcessResult.class.cast(object);
        logger.info("调用时间是: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(before)) + " 调用处理器类型: " + (result.isType() ? "自动调用" : "手动调用") + " 任务类型: " + ((ProxyInstance) this).object.getClass().getSimpleName() + " 任务信息: " + new SerializableUtil().toJson(field.get(this.object)) + " 调用结果: " + result.getMessage() + " 调用使用了: " + result.getTime());
        return object;
    }

}
