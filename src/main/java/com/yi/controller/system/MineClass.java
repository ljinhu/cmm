package com.yi.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysClass;
import com.yi.entity.SysUser;
import com.yi.service.ISysClassService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
