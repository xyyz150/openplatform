package com.opf.sample.config.listener;


import cn.hutool.core.date.DateUtil;
import com.github.opf.event.AfterDoServiceEvent;
import com.github.opf.event.OpfEventListener;
import com.github.opf.marshaller.MessageMarshallerUtils;
import com.github.opf.request.OpfRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by xyyz150
 */
@Component
public class WriteLogAfterDoServiceEventListener implements OpfEventListener<AfterDoServiceEvent> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void onOpfEvent(AfterDoServiceEvent opfEvent) {
        OpfRequestContext opfRequestContext = opfEvent.getOpfRequestContext();
        if (opfRequestContext != null) {
            Map<String, String> allParams = opfRequestContext.getAllParams();
            String requestMessage = MessageMarshallerUtils.asUrlString(allParams)+"&body="+opfRequestContext.getBody();
            Object response = opfRequestContext.getOpfResponse();

            String responseMessage = MessageMarshallerUtils.getMessage(response, opfRequestContext.getFormat());
            logger.info("本次请求" + opfRequestContext.getMethod() + "(" + opfRequestContext.getVersion() + ")耗时："
                    + DateUtil.formatDateTime(new Date(opfRequestContext.getServiceBeginTime())) + "到"
                    + DateUtil.formatDateTime(new Date(opfRequestContext.getServiceEndTime())) + "\n"
                    + "请求数据：" + requestMessage + "\n"+ "返回数据：" + responseMessage);
        }
    }


    public int getOrder() {
        return 0;
    }
}
