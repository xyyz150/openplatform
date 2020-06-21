package com.github.opf.router;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xyyz150
 */
public interface OpfRouter {
    /**
     * opf框架的总入口，一般框架实现，开发者无需关注。
     *
     * @param request
     * @param response
     */
    void service(HttpServletRequest request, HttpServletResponse response);

    /**
     * 初始化设置Spring的上下文
     *
     */
    void init();

    /**
     * servlet销毁
     */
    void destroy();
}
