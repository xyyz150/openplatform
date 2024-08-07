package com.github.opf.session;

/**
 * 会话管理器
 * Created by xyyz150
 */
public interface SessionManager {

    /**
     * 注册一个会话
     *
     * @param session
     */
    void addSession(String sessionId, Session session);

    /**
     * 从注册表中获取会话
     *
     * @param sessionId
     * @return
     */
    Session getSession(String sessionId);

    /**
     * 移除这个会话
     *
     * @param sessionId
     * @return
     */
    void removeSession(String sessionId);
}

