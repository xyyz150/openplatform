
package com.github.opf.event;

import com.github.opf.OpfContext;

/**
 * Created by xyyz150
 */
public class PreCloseOpfEvent extends OpfEvent {
    public PreCloseOpfEvent(Object source, OpfContext opfContext) {
        super(source, opfContext);
    }
}

