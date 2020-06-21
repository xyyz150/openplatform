package com.github.opf.request;

/**
 * Created by xyyz150
 *  opf服务的请求对象，请求方法的入参必须是继承于该类
 */
public interface OpfRequest {

    /**
     * 获取服务方法的上下文
     *
     * @return
     */
    OpfRequestContext getOpfRequestContext();

    public void setOpfRequestContext(OpfRequestContext opfRequestContext);
}
