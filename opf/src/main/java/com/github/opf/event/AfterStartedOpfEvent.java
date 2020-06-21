
package com.github.opf.event;

import com.github.opf.OpfContext;

/**
 * <pre>
 *   在opf框架初始化后产生的事件
 * </pre>
 *
 * @version 1.0
 */
public class AfterStartedOpfEvent extends OpfEvent {

    public AfterStartedOpfEvent(Object source, OpfContext opfContext) {
        super(source, opfContext);
    }

}

