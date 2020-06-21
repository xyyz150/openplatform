
package com.github.opf.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * <pre>
 *    指定自定义的系统参数名
 * </pre>
 *
 * @version 1.0
 */
public class SystemParameterNamesBeanDefinitionParser implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String appKey = element.getAttribute("appKey-alias");
        String accessToken = element.getAttribute("accessToken-alias");
        String method = element.getAttribute("method-alias");
        String version = element.getAttribute("version-alias");
        String format = element.getAttribute("format-alias");
        String sign = element.getAttribute("sign-alias");

        if (StringUtils.hasText(appKey)) {
            SystemParameterNames.setAppKey(appKey);
        }
        if (StringUtils.hasText(accessToken)) {
            SystemParameterNames.setAccessToken(accessToken);
        }
        if (StringUtils.hasText(method)) {
            SystemParameterNames.setMethod(method);
        }
        if (StringUtils.hasText(version)) {
            SystemParameterNames.setVersion(version);
        }
        if (StringUtils.hasText(format)) {
            SystemParameterNames.setFormat(format);
        }
        if (StringUtils.hasText(sign)) {
            SystemParameterNames.setSign(sign);
        }
        return null;
    }
}

