package com.opf.sample.config;

import com.github.opf.session.Session;
import com.github.opf.session.SessionManager;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author: ssf
 * @Date: 2024/07/22 11:52
 * @Description :
 */
@Component
public class MySessionManager implements SessionManager {
    public void addSession(String sessionId, Session session) {

    }

    public Session getSession(String sessionId) {
        if(sessionId.equals("session1")){
            return new Session() {
                public void setAttribute(String name, Object obj) {

                }

                public Object getAttribute(String name) {
                    return null;
                }

                public Map<String, Object> getAllAttributes() {
                    return null;
                }

                public void removeAttribute(String name) {

                }

                public boolean isChanged() {
                    return false;
                }
            };
        }
        return null;
    }

    public void removeSession(String sessionId) {

    }
}
