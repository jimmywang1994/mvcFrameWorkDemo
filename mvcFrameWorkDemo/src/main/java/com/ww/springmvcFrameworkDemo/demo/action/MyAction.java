package com.ww.springmvcFrameworkDemo.demo.action;

import com.ww.springmvcFrameworkDemo.demo.service.IDemoService;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCAutowired;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCController;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCRequestMapping;
import com.ww.springmvcFrameworkDemo.mvcFramework.annotation.MVCRequestParam;

@MVCController
@MVCRequestMapping("/my")
public class MyAction {
    @MVCAutowired
    private IDemoService demoService;

    @MVCRequestMapping("/query.json")
    public void query(@MVCRequestParam("name")String name){

    }

    @MVCRequestMapping("/edit.json")
    public void edit(@MVCRequestParam("id")String id){

    }
}
