package com.yi.controller;

import com.yi.common.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 监控
 */
@Controller
@RequestMapping("/system/monitor")
public class MonitorController extends BaseController {
	
	/**
	 * 系统监控列表
	 */
	@RequiresPermissions("monitorList")
    @RequestMapping("/list")  
    public  String list(Model model){
		return "system/monitor/list";
    } 
}
