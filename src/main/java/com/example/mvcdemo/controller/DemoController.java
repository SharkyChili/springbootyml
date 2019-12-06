package com.example.mvcdemo.controller;

import com.example.mvcdemo.Entity;
import com.example.mvcdemo.annotation.Login;
import com.example.mvcdemo.annotation.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    Environment environment;

    @Login
    @RequestMapping("/method")
    public Entity method(@LoginUser Entity entity, @RequestBody Entity entity1){
        System.out.println(environment.getProperty("num"));

        return entity;
    }
}
