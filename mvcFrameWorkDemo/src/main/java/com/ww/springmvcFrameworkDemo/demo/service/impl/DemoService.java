package com.ww.springmvcFrameworkDemo.demo.service.impl;

import com.ww.springmvcFrameworkDemo.demo.service.IDemoService;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCService;

@MVCService
public class DemoService implements IDemoService {
    @Override
    public String get(String name) {
        return "name is:" + name;
    }
}
