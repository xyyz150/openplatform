package com.qimenapi.util;

import com.srop.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xyyz150 on 2016/1/12.
 */
public class WebUtils {
    public static Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static <ReturnT> ReturnT doPost(String url, Object parameter, Class<ReturnT> returnClass) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        String str = JsonUtils.writeValueAsString(parameter);
        StringEntity entity= new StringEntity(str, "UTF-8");
        String resData="";
        try {
            entity.setContentType("application/json");
            httppost.setEntity(entity);
            httppost.setHeader("ContentType", "application/json");
            HttpResponse response = client.execute(httppost);
            HttpEntity entityResult = response.getEntity();
            resData = EntityUtils.toString(entityResult);
            return JsonUtils.readObject(resData, returnClass);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.error(url+resData);//记录请求地址和返回错误信息
            throw  ex;
        }
        finally {
            EntityUtils.consume(entity);//关闭连接
        }
    }
    public static <ReturnT> ReturnT doGet(String url, Class<ReturnT> returnClass) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(url);
        String resData="";
        try {
            httppost.setHeader("ContentType", "application/json");
            HttpResponse response = client.execute(httppost);
            HttpEntity entityResult = response.getEntity();
            resData = EntityUtils.toString(entityResult);
            return JsonUtils.readObject(resData, returnClass);
        }
        catch (Exception ex)
        {
            logger.error(url+resData);//记录请求地址和返回错误信息
            throw  ex;
        }

    }

}
