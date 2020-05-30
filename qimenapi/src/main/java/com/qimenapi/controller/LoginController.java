package com.qimenapi.controller;

import com.qimenapi.util.HttpHelper;
import com.srop.Constants;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xyyz150 on 2016/1/7.
 */
@Controller
public class LoginController {
    @RequestMapping(value = "/login")
    public String showLoginForm(HttpServletRequest req, Model model) {
        String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (exceptionClassName != null) {
            error = "其他错误：" + exceptionClassName;
        }
        model.addAttribute("error", error);
        return "login";
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String test(HttpServletRequest req, HttpServletResponse res) throws Exception {
//        StringBuffer str = new StringBuffer();
//        InputStreamReader reader1 = null;
//        int len = req.getContentLength();
//        int total = 0;
//        byte buffer[];
//        buffer = new byte[len];
//        try {
//            ServletInputStream reader = req.getInputStream();
//            String inputLine;
//            for (int once = 0; total < len && once >= 0; total += once) {
//                once = reader.readLine(buffer, total, len);
//            }
//        } catch (Exception e) {
//            System.out.print(e);
//        } finally {
//        }
//        String ss= new String(buffer, Constants.UTF8);
//        return ss;
        String ss= HttpHelper.getStreamAsString(req.getInputStream(),Constants.UTF8);
        return ss;
    }
}
