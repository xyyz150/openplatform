package com.srop.request;

/**
 * Created by xyyz150 on 2016/1/5.
 *  srop服务的请求对象，请求方法的入参必须是继承于该类
 */
public interface SropRequest {

    /**
     * 获取服务方法的上下文
     *
     * @return
     */
    SropRequestContext getSropRequestContext();

    public void setSropRequestContext(SropRequestContext iceRequestContext);
}
