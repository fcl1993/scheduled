package com.dafy.scheduled;

import com.dafy.scheduled.core.http.HttpRequest;
import com.fuchenglei.util.SerializableUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Random;

public class TaskTest
{

    //@Test
    public void httpJob()
    {

        /*for (int i = 0; i < 1000; i++)
        {

            HashMap<String, Object> mp = new HashMap<String, Object>();
            HashMap<String, Object> args = new HashMap<String, Object>();
            mp.put("name", "JOB测试");
            //mp.put("time", new Random().nextInt(20) * 1000);
            mp.put("time", 0);
            mp.put("url", "localhost:8080/handleDataJob");
            mp.put("type", "POST");
            mp.put("overTime", 5 * 1000);
            args.put("name", "name");
            args.put("age", 7);
            mp.put("args", new SerializableUtil<HashMap<String, Object>>().toJson(args));
            HttpRequest.requestPost("localhost/createHttpJob", mp, 5000);
        }*/

//        while (true)
//        {

            HashMap<String, Object> mp = new HashMap<String, Object>();
            HashMap<String, Object> args = new HashMap<String, Object>();
            mp.put("name", "JOB测试");
            mp.put("time", new Random().nextInt(20) * 1000);
            mp.put("url", "localhost:8080/handleDataJob");
            mp.put("type", "POST");
            mp.put("overTime", 5 * 1000);
            mp.put("innerSecurity", "44d052180b404524a6c9541f914314a8");
            args.put("name", "name");
            args.put("age", 6);
            mp.put("args", new SerializableUtil<HashMap<String, Object>>().toJson(args));
            HttpRequest.requestPost("localhost/task/createHttpJob", mp, 5000);
            try
            {
                Thread.sleep((new Random().nextInt(30) + 10) * 1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
//        }
    }

}
