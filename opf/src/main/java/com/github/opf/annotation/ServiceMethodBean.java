
package com.github.opf.annotation;

import com.github.opf.handler.ServiceMethodDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 在服务类中标该类，以便确定服务方法所属的组及相关信息。由于ApiMethodGroup已经标注了
 * Spring的{@link Component}注解，因此标注了{@link ServiceMethodBean}的类自动成为Spring的Bean.
 * Created by xyyz150
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface ServiceMethodBean {

    String value() default "";
    /**
     * 所属的服务分组，默认为"DEFAULT"
     */
    String group() default ServiceMethodDefinition.DEFAULT_GROUP;

    /**
     * 标签，可以打上多个标签
     */
    String[] tags() default {};

    /**
     * 访问过期时间，单位为毫秒，即大于这个过期时间的链接会结束并返回错误报文，如果
     * 为0或负数则表示不进行过期限制
     */
    int timeout() default -1;

    /**
     * 该方法所对应的版本号，对应version请求参数的值
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

