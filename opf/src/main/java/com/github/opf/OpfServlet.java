
package com.github.opf;

import com.github.opf.router.OpfRouter;
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


public class OpfServlet extends HttpServlet {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private OpfRouter serviceRouter;


    /**
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
        this.serviceRouter = ctx.getBean(OpfRouter.class);
        if (this.serviceRouter == null) {
            logger.error("在Spring容器中未找到" + OpfRouter.class.getName() +
                    "的Bean,请在Spring中配置opf框架。");
        }
    }

    private ApplicationContext getApplicationContext(ServletConfig servletConfig) {
        return (ApplicationContext) servletConfig.getServletContext().getAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }
}

