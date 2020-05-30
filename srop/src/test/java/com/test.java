package com;

import com.srop.request.SropRequest;
import com.srop.utils.XStreamUtils;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * Created by xyyz150 on 2016/1/14.
 */
public class test {

    @Pattern(regexp = "\\w{4,30}")
    private String aa;

    @NotNull
    private String bb;

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    @Test
    public void testvalidation() throws UnsupportedEncodingException {
        test t = new test();
//        LocalValidatorFactoryBean factoryBean=new LocalValidatorFactoryBean();
//        factoryBean.afterPropertiesSet();
//        Set<ConstraintViolation<test>> violations =factoryBean.validate(t);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<test>> violations = validator.validate(t);
        String str = "";
        for (ConstraintViolation<test> violation : violations) {
            str += violation.getPropertyPath() + violation.getMessage();
        }
        System.out.print(str);
        byte[] b = str.getBytes("gb2312");//编码
        String sa = new String(b,"gb2312");//解码:用什么字符集编码就用什么字符集解码
        System.out.println(sa);

//        b = sa.getBytes("utf-8");//编码
        String sa1 = new String(sa.getBytes(), "utf-8");//解码
        System.err.println(sa1);
    }

    @Test
    public void convertionString() throws UnsupportedEncodingException {
        String s = "platOrderHeader不能为null;";
        byte[] b = s.getBytes("gb2312");//编码
        String sa = new String(b,"gb2312");//解码:用什么字符集编码就用什么字符集解码
        System.out.println(sa);

//        b = sa.getBytes("utf-8");//编码
        String sa1 = new String(sa.getBytes(), "utf-8");//解码
        System.err.println(sa1);
        System.out.println(Charset.defaultCharset().name());
    }

}
