package com.github.opf.autoConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: xyyz150
 * Date: 2020/6/21 13:36
 * Description:线程池
 */
@ConfigurationProperties(prefix = "opf.event")
public class OpfEventExecutorConfiguration {
    //线程的最少数量
    private Integer corePoolSize;
    //线程所允许的空闲时间,s
    private Integer keepAliveSeconds;
    //线程的最大数量
    private Integer maxPoolSize;
    //线程池所使用的缓冲队列
    private Integer queueCapacity;

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}
