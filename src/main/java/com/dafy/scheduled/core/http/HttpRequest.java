package com.dafy.scheduled.core.http;

import com.dafy.scheduled.core.process.AbstractTaskProcess;
import com.fuchenglei.util.SerializableUtil;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * http
 *
 * @author 付成垒
 */
public class HttpRequest
{

    private static ConcurrentHashMap<String, CloseableHttpClient> container = new ConcurrentHashMap<String, CloseableHttpClient>();

    private static ReentrantLock lock = new ReentrantLock();

    private HttpRequest()
    {
    }

    private static void requestConfig(HttpRequestBase httpRequestBase, Integer overTime)
    {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Integer.parseInt(AbstractTaskProcess.scheduled.getString("connectionRequestTimeout")))   //从连接池获取链接超时时间
                .setConnectTimeout(Integer.parseInt(AbstractTaskProcess.scheduled.getString("connectTimeout"))) //请求链接超时时间
                .setSocketTimeout(overTime) //数据传输超时时间
                .build();
        httpRequestBase.setConfig(requestConfig);
    }

    public static CloseableHttpClient getHttpClient(String url)
    {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":"))
        {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        CloseableHttpClient httpClient = container.get(hostname);
        lock.lock();
        if (httpClient == null)
        {
            httpClient = createHttpClient(Integer.parseInt(AbstractTaskProcess.scheduled.getString("hostMaxTotal")), Integer.parseInt(AbstractTaskProcess.scheduled.getString("hostDefaultMaxTotal"))/*, 100, hostname*/);
            container.put(hostname, httpClient);
        }
        lock.unlock();
        return httpClient;
    }

    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute/*, int maxRoute, String hostname*/)
    {
        ConnectionSocketFactory connectionSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory layeredConnectionSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", connectionSocketFactory).register("https", layeredConnectionSocketFactory).build();
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(maxTotal);  //设置最大连接数200
        manager.setDefaultMaxPerRoute(maxPerRoute); //默认路由数
        //HttpHost httpHost = new HttpHost(hostname);   //针对的主机
        //manager.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);  //最大路由数   因为没有多个主机端口连接 暂时不控制最大路由
        manager.setValidateAfterInactivity(Integer.parseInt(AbstractTaskProcess.scheduled.getString("validateAfterInactivity")));       //检查空闲链接
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler()
        {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context)
            {
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                org.apache.http.HttpRequest request = clientContext.getRequest();
                if (!(request instanceof HttpEntityEnclosingRequest))
                {
                    return true;
                }
                if (executionCount >= 3)
                {
                    throw new HttpException(3, HttpReturn.entity.get(3));
                }
                else if (exception instanceof UnknownHostException)
                {
                    throw new HttpException(4, HttpReturn.entity.get(4));
                }
                else if (exception instanceof NoHttpResponseException)
                {
                    throw new HttpException(5, HttpReturn.entity.get(5));
                }
                //任务超时
                else if (exception instanceof SocketTimeoutException)
                {
                    throw new HttpException(6, HttpReturn.entity.get(6));
                }
                else if (exception instanceof ConnectTimeoutException)
                {
                    throw new HttpException(7, HttpReturn.entity.get(7));
                }
                else if (exception instanceof HttpHostConnectException)
                {
                    throw new HttpException(8, HttpReturn.entity.get(8));
                }
                else if (exception instanceof BindException)
                {
                    throw new HttpException(9, HttpReturn.entity.get(9));
                }
                //暂时没有ssl
                /*else if (exception instanceof SSLHandshakeException)
                {
                    throw new HttpException(11, HttpReturn.entity.get(11));
                }
                else if (exception instanceof SSLException)
                {
                    throw new HttpException(12, HttpReturn.entity.get(12));
                }*/
                else
                    throw new HttpException(10, "其它未知的异常,异常描述:" + (exception != null ? exception.getClass().getName() : null));
            }
        };
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(manager)
                .setConnectionManagerShared(true)
                .evictIdleConnections(10, TimeUnit.SECONDS)//定期回收空闲连接
                .evictExpiredConnections()//回收过期连接
                .setRetryHandler(httpRequestRetryHandler).build();
        return httpClient;
    }

    private static void AssemblyParams(HttpPost httpPost, Map<String, Object> params)
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet)
        {
            nameValuePairs.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public static HttpReturn requestPost(String url, Map<String, Object> params, Integer overTime)
    {
        HttpPost httpPost = new HttpPost("http://" + url);
        requestConfig(httpPost, overTime);
        AssemblyParams(httpPost, params);
        CloseableHttpResponse response = null;
        try
        {
            response = getHttpClient("http://" + url).execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            if (!"false".equals(result) && !"true".equals(result))
                return new HttpReturn(2, HttpReturn.entity.get(2));
            if ("false".equals(result))
                return new HttpReturn(1, HttpReturn.entity.get(1));
            return new HttpReturn(0, HttpReturn.entity.get(0));
        }
        catch (Exception e)
        {
            if (e instanceof HttpException)
                return new HttpReturn(HttpException.class.cast(e).getCode(), HttpException.class.cast(e).getMessage());
            //保险操作
            return new HttpReturn(10, HttpReturn.entity.get(10));
        }
        finally
        {
            try
            {
                if (response != null)
                    response.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static HttpReturn requestGet(String url, Integer overTime)
    {
        HttpGet httpGet = new HttpGet("http://" + url);
        requestConfig(httpGet, overTime);
        CloseableHttpResponse response = null;
        try
        {
            response = getHttpClient("http://" + url).execute(httpGet, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            if (!"false".equals(result) && !"true".equals(result))
                return new HttpReturn(2, HttpReturn.entity.get(2));
            if ("false".equals(result))
                return new HttpReturn(1, HttpReturn.entity.get(1));
            return new HttpReturn(0, HttpReturn.entity.get(0));
        }
        catch (Exception e)
        {
            if (e instanceof HttpException)
                return new HttpReturn(HttpException.class.cast(e).getCode(), HttpException.class.cast(e).getMessage());
            //保险操作
            return new HttpReturn(10, HttpReturn.entity.get(10));
        }
        finally
        {
            try
            {
                if (response != null)
                    response.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static HttpReturn requestHttpHandOutPatternPost(String url, Map<String, Object> params, Integer overTime)
    {
        HttpPost httpPost = new HttpPost("http://" + url);
        requestConfig(httpPost, overTime);
        AssemblyParams(httpPost, params);
        CloseableHttpResponse response = null;
        try
        {
            response = getHttpClient("http://" + url).execute(httpPost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            List<Object> data = new ArrayList<Object>();
            try
            {
                data = new SerializableUtil<List<Object>>().toObject(List.class, result);
            }
            catch (Exception exception)
            {
                return new HttpReturn(2, HttpReturn.entity.get(2));
            }
            return new HttpReturn(0, HttpReturn.entity.get(0), data);
        }
        catch (Exception e)
        {
            if (e instanceof HttpException)
                return new HttpReturn(HttpException.class.cast(e).getCode(), HttpException.class.cast(e).getMessage());
            //保险操作
            return new HttpReturn(10, HttpReturn.entity.get(10));
        }
        finally
        {
            try
            {
                if (response != null)
                    response.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static HttpReturn requestHttpHandOutPatternGet(String url, Integer overTime)
    {
        HttpGet httpGet = new HttpGet("http://" + url);
        requestConfig(httpGet, overTime);
        CloseableHttpResponse response = null;
        try
        {
            response = getHttpClient("http://" + url).execute(httpGet, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            List<Object> data = new ArrayList<Object>();
            try
            {
                data = new SerializableUtil<List<Object>>().toObject(List.class, result);
            }
            catch (Exception exception)
            {
                return new HttpReturn(2, HttpReturn.entity.get(2));
            }
            return new HttpReturn(0, HttpReturn.entity.get(0), data);
        }
        catch (Exception e)
        {
            if (e instanceof HttpException)
                return new HttpReturn(HttpException.class.cast(e).getCode(), HttpException.class.cast(e).getMessage());
            //保险操作
            return new HttpReturn(10, HttpReturn.entity.get(10));
        }
        finally
        {
            try
            {
                if (response != null)
                    response.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
