package com.yi.controller;

import com.yi.common.controller.BaseController;
import com.yi.service.ISysClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system/mine")
public class MineClass extends BaseController {
    @Autowired
    private ISysClassService sysClassService;
    private final String PREFIX="system/class/";

    @RequestMapping("/test")
    public String test(){
        return PREFIX +"test";
    }
}
