
package com.srop.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class SropNamespaceHandler extends NamespaceHandlerSupport {


    public void init() {
        registerBeanDefinitionParser("router", new SropDispatchBeanDefinitionParser());
        registerBeanDefinitionParser("listeners", new SropListenersBeanDefinitionParser());
        registerBeanDefinitionParser("sysparams", new SystemParameterNamesBeanDefinitionParser());
    }
}

