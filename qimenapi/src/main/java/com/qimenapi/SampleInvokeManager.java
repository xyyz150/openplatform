package com.qimenapi;

import com.srop.security.InvokeManager;
import com.srop.session.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xyyz150 on 2016/8/18.
 */
public class SampleInvokeManager implements InvokeManager {
    ConcurrentHashMap<String, ArrayList<Long>> recodeList = new ConcurrentHashMap<String, ArrayList<Long>>();

    final static int INVOKE_COUNT = 250;

    @Override
    public void caculateInvokeTimes(String appKey, Session session) {

    }

    @Override
    public boolean isUserInvokeLimitExceed(String appKey, Session session) {
        return false;
    }

    @Override
    public boolean isSessionInvokeLimitExceed(String appKey, String sessionId) {
        return false;
    }

    @Override
    public boolean isAppInvokeLimitExceed(String appKey) {
        return false;
    }

    @Override
    public boolean isAppInvokeFrequencyExceed(String appKey) {
        return false;
    }

    /**
     * 单个接口1秒钟频率不超过INVOKE_COUNT
     *
     * @param appKey
     * @param sessionId
     * @param methodName
     * @param version
     * @return
     */
    public boolean isSessionApiInvokeFrequencyExceed(String appKey, String sessionId, String methodName, String version) {
        String key = sessionId + methodName + version;
        ArrayList<Long> queue;
        if (recodeList.containsKey(key)) {
            queue = recodeList.get(key);
        } else {
            queue = new ArrayList<Long>();
            recodeList.put(key, queue);
        }
        synchronized (queue) {
            long nowDate = System.currentTimeMillis();
            while (queue.size() > 0) {
                long first = queue.get(0);
                if (nowDate - first >= 1000) {
                    queue.remove(0);
                } else {
                    break;
                }
            }
            queue.add(nowDate);
            if (queue.size() >= INVOKE_COUNT) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void countTimes(String appKey) {

    }

    @Override
    public boolean invokeLimit(String appKey) {
        return false;
    }
}
