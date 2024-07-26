
package com.github.opf.annotation;


import com.github.opf.handler.ServiceMethodDefinition;

import java.lang.annotation.*;

/**
 * 使用该注解对服务方法进行标注，这些方法必须是Spring的Service:既打了@Service的注解。
 * Created by xyyz150
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceMethod {

    /**
     * 服务的方法名，即由method参数指定的服务方法名
     */
    String method() default "";

    /**
     * 所属的服务分组
     */
    String group() default ServiceMethodDefinition.DEFAULT_GROUP;

    /**
     * 标签，可以打上多个标签
     */
    String[] tags() default {};

    /**
     * 服务的中文名称
     */
    String title() default "";

    /**
     * 访问过期时间，单位为毫秒，即大于这个过期时间的链接会结束并返回错误报文，如果
     * 为0或负数则表示不进行过期限制
     */
    int timeout() default -1;

    /**
     * 该方法所对应的版本号，对应version请求参数的值，版本为空，表示不进行版本限定
     */
    String version() default "1.0";

    /**
     * 服务方法需要需求会话检查，默认要检查
     */
    NeedInSessionType needInSession() default NeedInSessionType.DEFAULT;

    /**
     * 是否忽略签名检查，默认不忽略
     */
    IgnoreSignType ignoreSign() default IgnoreSignType.DEFAULT;

    /**
     * 服务方法是否已经过期，默认不过期
     */
    DeprecatedType deprecated() default  DeprecatedType.DEFAULT;
}
