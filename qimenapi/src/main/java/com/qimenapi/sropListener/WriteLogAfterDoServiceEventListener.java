package com.qimenapi.sropListener;


import com.qimenapi.util.DateHelp;
import com.srop.event.AfterDoServiceEvent;
import com.srop.event.SropEventListener;
import com.srop.marshaller.MessageMarshallerUtils;
import com.srop.request.SropRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created by xyyz150 on 2015/6/27.
 */
public class WriteLogAfterDoServiceEventListener implements SropEventListener<AfterDoServiceEvent> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    public void onRopEvent(AfterDoServiceEvent ropEvent) {
        SropRequestContext ropRequestContext = ropEvent.getRopRequestContext();
        if (ropRequestContext != null) {
            Map<String, String> allParams = ropRequestContext.getAllParams();
            String requestmessage = MessageMarshallerUtils.asUrlString(allParams)+"&body="+ropRequestContext.getBody();
            Object response = ropRequestContext.getSropResponse();

            String responsemessage = MessageMarshallerUtils.getMessage(response, ropRequestContext.getFormat());
            logger.error("本次请求" + ropRequestContext.getMethod() + "(" + ropRequestContext.getVersion() + ")耗时：" + DateHelp.format(new Date(ropRequestContext.getServiceBeginTime())) + "到" + DateHelp.format(new Date(ropRequestContext.getServiceEndTime())) + "\n"
                    + "请求数据：" + requestmessage + "\n"
                    + "返回数据：" + responsemessage);
        }
    }


    public int getOrder() {
        return 0;
    }
}
