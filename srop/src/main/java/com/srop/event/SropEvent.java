
package com.srop.event;

import com.srop.SropContext;

import java.util.EventObject;


public abstract class SropEvent extends EventObject {

    private SropContext ropContext;

    public SropEvent(Object source, SropContext ropContext) {
        super(source);
        this.ropContext = ropContext;
    }

    public SropContext getRopContext() {
        return ropContext;
    }
}

