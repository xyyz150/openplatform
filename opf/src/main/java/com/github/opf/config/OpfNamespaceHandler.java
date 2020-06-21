
package com.github.opf.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class OpfNamespaceHandler extends NamespaceHandlerSupport {


    public void init() {
        registerBeanDefinitionParser("router", new OpfDispatchBeanDefinitionParser());
        registerBeanDefinitionParser("listeners", new OpfListenersBeanDefinitionParser());
        registerBeanDefinitionParser("sysParams", new SystemParameterNamesBeanDefinitionParser());
    }
}

