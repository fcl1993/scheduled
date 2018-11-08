package com.dafy.scheduled.web.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * task util    暂不增加并发支持
 *
 * @author 付成垒
 */
public class TaskUtil
{

    private static String httpJob = null;

    private static String security = null;

    //   示例http://localhost/createHttpJob  创建httpJob
    public static boolean submitJob(HashMap<String, Object> parameter)
    {
        boolean result = false;
        try
        {
            if (security == null)
            {
                synchronized (security)
                {
                    security = Env.environment.getProperty("task.security");
                }
            }
            parameter.put("innerSecurity", security);
            if (parameter.get("time") == null) parameter.put("time", 0);
            if (parameter.get("executionCycle") != null) parameter.remove("executionCycle");
            if (parameter.get("overTime") == null) parameter.put("overTime", null);
            if (parameter.get("url") == null) return result;
            if (parameter.get("type") == null) parameter.put("type", "POST");
            Set<String> keys = parameter.keySet();
            HashMap<String, Object> mp = new HashMap<String, Object>();
            Iterator<String> obj = keys.iterator();
            while (obj.hasNext())
            {
                String arg = obj.next();
                if (!"time".equals(arg) && !"executionCycle".equals(arg) && !"overTime".equals(arg) && !"url".equals(arg) && !"type".equals(arg))
                {
                    mp.put(arg, parameter.get(arg));
                }
            }
            parameter.put("args", JSONObject.toJSONString(mp));
            if (httpJob == null)
            {
                synchronized (httpJob)
                {
                    httpJob = Env.environment.getProperty("task.httpJob");
                }
            }
            result = TaskUtil.doPost(httpJob, parameter);
        }
        catch (Exception e)
        {
            result = false;
            return result;
        }
        return result;
    }

    /**
     * 发post请求
     *
     * @param url
     * @return
     */
    public static boolean doPost(String url, HashMap<String, Object> mp) throws Exception
    {
        int i = 0;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        Set<String> keys = mp.keySet();
        Iterator<String> iterator = keys.iterator();
        List<NameValuePair> nvps = new ArrayList<>();
        while (iterator.hasNext())
        {
            String obj = iterator.next();
            nvps.add(new BasicNameValuePair(obj, String.valueOf(mp.get(obj))));

        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        if (!"false".equals(result) && !"true".equals(result)) return false;
        return Boolean.valueOf(result);
    }

    @Component
    public static class Env
    {

        public static Environment environment;

        @Autowired
        public Env(Environment env)
        {
            this.environment = env;
        }

    }

}