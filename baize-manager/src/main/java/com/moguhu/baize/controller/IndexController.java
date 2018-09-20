package com.moguhu.baize.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController extends BaseController {

    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index/index");
        return mav;
    }

    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("index/home");
        return mav;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world!";
    }

    @RequestMapping("/getimage")
    public ModelAndView getimage() {
        ModelAndView mav = new ModelAndView("index/get-image");
        return mav;
    }
}
