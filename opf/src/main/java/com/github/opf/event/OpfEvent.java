
package com.github.opf.event;

import com.github.opf.OpfContext;

import java.util.EventObject;


public abstract class OpfEvent extends EventObject {

    private OpfContext opfContext;

    public OpfEvent(Object source, OpfContext opfContext) {
        super(source);
        this.opfContext = opfContext;
    }

    public OpfContext getOpfContext() {
        return opfContext;
    }
}

