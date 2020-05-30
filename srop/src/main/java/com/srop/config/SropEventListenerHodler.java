
package com.srop.config;

import com.srop.event.SropEventListener;

/**
 * <pre>
 * 功能说明：
 * </pre>
 *
 * @version 1.0
 */
public class SropEventListenerHodler {

    private SropEventListener ropEventListener;

    public SropEventListenerHodler(SropEventListener ropEventListener) {
        this.ropEventListener = ropEventListener;
    }

    public SropEventListener getRopEventListener() {
        return ropEventListener;
    }
}

