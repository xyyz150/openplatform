package com.qimenapi.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by xyyz150 on 2016/9/13.
 */
public class SystemInit implements InitializingBean, ServletContextAware {
    @Autowired
    ToolService toolService;

    @Override
    public void afterPropertiesSet() throws Exception {
        toolService.InitApiList();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
