package com.dafy.scheduled.core.process;

import com.dafy.scheduled.core.http.HttpRequest;
import com.dafy.scheduled.core.http.HttpReturn;
import com.dafy.scheduled.core.data.HandOut;
import com.dafy.scheduled.core.service.HandOutService;
import com.fuchenglei.core.container.Container;
import com.fuchenglei.util.SerializableUtil;
import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * httpTask
 *
 * @author 付成垒
 */
public class HandOutDestinationProcess extends AbstractTaskProcess implements Callable<Integer>
{

    private static Logger logger = Logger.getLogger(HandOutDestinationProcess.class);

    private HandOutService httpTaskService = Container.obtainBean(HandOutService.class);

    private HandOut metaData;

    private String data;

    private LinkedBlockingDeque queue;

    public HandOutDestinationProcess(HandOut metaData, String data, LinkedBlockingDeque queue)
    {
        super();
        this.queue = queue;
        this.metaData = metaData;
        this.data = data;
    }

    public Integer call() throws InterruptedException
    {
        executor();
        queue.take();
        return this.state;
    }

    @Override
    public void executor()
    {
        try
        {
            HttpReturn httpReturn = null;
            if (metaData.getnParameterType() == 0)
            {
                if ("GET".equals(this.metaData.getStrDestinationType().trim()))
                {
                    httpReturn = HttpRequest.requestGet(this.metaData.getStrDestinationURL() + "?data=" + URLEncoder.encode(this.data, "utf-8"), metaData.getnHandleTime());
                }
                else if ("POST".equals(this.metaData.getStrDestinationType().trim()))
                {
                    Map data = new HashMap();
                    data.put("data", this.data);
                    httpReturn = HttpRequest.requestPost(this.metaData.getStrDestinationURL(), data, metaData.getnHandleTime());
                }
            }
            else    //其它均为MAP
            {
                Map data = null;
                try
                {
                    data = new SerializableUtil<HashMap>().toObject(HashMap.class, this.data);
                }
                catch (Exception exception)
                {
                    data = new HashMap();
                }
                String url = "";
                if (!data.isEmpty() && "GET".equals(this.metaData.getStrDestinationType().trim()))
                {
                    StringBuilder builder = new StringBuilder("");
                    builder.append("?");
                    Iterator<String> keys = data.keySet().iterator();
                    while (keys.hasNext())
                    {
                        String key = keys.next();
                        builder.append(key + "=" + URLEncoder.encode(String.valueOf(data.get(key)), "utf-8") + "&");
                    }
                    url = builder.toString();
                    url = url.substring(0, url.length() - 1);
                }
                if ("GET".equals(this.metaData.getStrDestinationType().trim()))
                {
                    httpReturn = HttpRequest.requestGet(this.metaData.getStrDestinationURL() + url, this.metaData.getnHandleTime());
                }
                else if ("POST".equals(this.metaData.getStrDestinationType().trim()))
                {
                    httpReturn = HttpRequest.requestPost(this.metaData.getStrDestinationURL(), data, this.metaData.getnHandleTime());
                }
            }
            if (httpReturn.getCode() != 0)
            {
                logger.error("Handle data dispose error : {\"taskInfo\":\"" + this.metaData.toString() + "\"data\":\"" + data + "\"errorMessage\":\"" + httpReturn.getMessage() + "\"}");
            }
        }
        catch (Exception exception)
        {
            logger.error("Handle data dispose error : {\"taskInfo\":\"" + this.metaData.toString() + "\"data\":\"" + data + "\"errorMessage\":\"" + "本次任务异常请检查任务配置" + "\"}");
        }
    }

}
