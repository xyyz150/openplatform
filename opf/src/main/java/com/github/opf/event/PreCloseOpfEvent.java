
package com.github.opf.event;

import com.github.opf.OpfContext;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class PreCloseOpfEvent extends OpfEvent {
    public PreCloseOpfEvent(Object source, OpfContext opfContext) {
        super(source, opfContext);
    }
}

