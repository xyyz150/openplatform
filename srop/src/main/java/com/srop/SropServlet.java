
package com.srop;

import com.srop.router.SropRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SropServlet extends HttpServlet {

    protected  Logger logger = LoggerFactory.getLogger(getClass());

    private SropRouter serviceRouter;


    /**
     *
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        serviceRouter.service(req, resp);
    }


    public void init(ServletConfig servletConfig) throws ServletException {
        ApplicationContext ctx = getApplicationContext(servletConfig);
        this.serviceRouter = ctx.getBean(SropRouter.class);
        if (this.serviceRouter == null) {
            logger.error("在Spring容器中未找到" + SropRouter.class.getName() +
                    "的Bean,请在Spring配置文件中通过<saop:router/>安装srop框架。");
        }
    }

    private ApplicationContext getApplicationContext(ServletConfig servletConfig) {
        return (ApplicationContext) servletConfig.getServletContext().getAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }
}

