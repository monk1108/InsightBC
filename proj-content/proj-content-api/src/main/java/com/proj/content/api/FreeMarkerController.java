package com.proj.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: Yinuo
 * @Date: 2023/10/25 20:09
 */
@Controller
public class FreeMarkerController {
    @GetMapping("/testfreemarker")
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView();
        // set model data
        modelAndView.addObject("name","Yinuo");
        // set template name (referring to test.ftl)
        modelAndView.setViewName("test");
        return modelAndView;
    }
}
