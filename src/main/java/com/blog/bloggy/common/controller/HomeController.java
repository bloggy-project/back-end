package com.blog.bloggy.common.controller;

import com.blog.bloggy.aop.token.AccessTokenRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {

    @GetMapping(value="/home")
    public String hello() {
        return "home";
    }



}
