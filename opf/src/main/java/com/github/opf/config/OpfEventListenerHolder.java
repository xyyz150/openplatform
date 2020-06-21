
package com.github.opf.config;

import com.github.opf.event.OpfEventListener;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class OpfEventListenerHolder {

    private OpfEventListener opfEventListener;

    public OpfEventListenerHolder(OpfEventListener opfEventListener) {
        this.opfEventListener = opfEventListener;
    }

    public OpfEventListener getOpfEventListener() {
        return opfEventListener;
    }
}

