package com.qimenapi.controller;

import com.qimenapi.entity.RequestPostParam;
import com.qimenapi.entity.RequestPostResult;
import com.qimenapi.entity.ServiceMethodInfo;
import com.qimenapi.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xyyz150 on 2016/1/7.
 */
@Controller
public class IndexController {
    @Autowired
    ToolService toolService;

    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }


    @RequestMapping("/tool")
    public String tool(Model model) {
        return "tool";
    }

    @RequestMapping(value = "/tool/getApiList", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> getApiList() {
        List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
        List<String> list = toolService.getApiList();
        if (list != null) {
            for (String s : list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", s);
                map.put("text", s);
                ret.add(map);
            }
        }
        return ret;
    }

    @RequestMapping(value = "/tool/getApiInfo", method = RequestMethod.GET)
    @ResponseBody
    public ServiceMethodInfo getApiInfo(String name) {
        return toolService.getApiInfo(name);
    }


    @RequestMapping(value = "/tool/postTest", method = RequestMethod.POST)
    @ResponseBody
    public RequestPostResult postTest(@RequestBody RequestPostParam parameter,HttpServletRequest req) {
        String url = "http://" + req.getServerName() + ":" + req.getServerPort()+"/router?";
        parameter.setUrl(url);
        return toolService.postParam(parameter);
    }
}
