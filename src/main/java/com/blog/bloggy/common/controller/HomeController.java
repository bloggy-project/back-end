package com.blog.bloggy.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {

    @GetMapping(value="/home")
    public String hello() {
        return "home";
    }

    @ResponseBody
    @GetMapping(value="/test")
    public Test test(){
        Test test=new Test();
        test.setName("테스트 성공");
        return test;
    }
    public class Test{
        String name;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
