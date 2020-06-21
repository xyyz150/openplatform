
package com.github.opf.event;

import com.github.opf.request.OpfRequestContext;

/**
 * <pre>
 *    在执行服务方法之前产生的事件
 * </pre>
 *

 * @version 1.0
 */
public class PreDoServiceEvent extends OpfEvent {

    private OpfRequestContext opfRequestContext;

    public PreDoServiceEvent(Object source, OpfRequestContext opfRequestContext) {
        super(source, opfRequestContext.getOpfContext());
        this.opfRequestContext = opfRequestContext;
    }

    public OpfRequestContext getOpfRequestContext() {
        return opfRequestContext;
    }

    public long getServiceBeginTime() {
        return opfRequestContext.getServiceBeginTime();
    }
}

