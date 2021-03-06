package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yi.common.anno.Log;
import com.yi.common.controller.BaseController;
import com.yi.pojo.SysSetting;
import com.yi.service.ISysSettingService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统设置控制器
 */
@Controller
@RequestMapping("/system/setting")
public class SettingController extends BaseController {
	
	@Autowired private ISysSettingService	sysSettingService;
	
	/**
	 * 查询系统设置
	 */
	@RequiresPermissions("listSetting")
    @RequestMapping("/page")  
    public  String page(Model model){
    	
    	List<SysSetting> list =  sysSettingService.selectList(new EntityWrapper<SysSetting>().orderBy("sort",true));
    	model.addAttribute("list",list);
		return "system/setting/page";
    } 
    
	@RequiresPermissions("doSetting")
    @Log("更新系统设置")
    @RequestMapping("/doSetting")
    public String doSetting(String[] id,String[] sysValue,Model model,RedirectAttributes redirectAttributes){
    	
    	List<SysSetting> sysSettings = new ArrayList<SysSetting>();
    	if(ArrayUtils.isNotEmpty(id)){
    		for(int i=0;i<id.length;i++){
    			SysSetting setting	= new SysSetting();
        		setting.setId(id[i]);
        		setting.setSysValue(sysValue[i]);
        		sysSettings.add(setting);
    		}
    	}
    	sysSettingService.updateBatchById(sysSettings);
    	redirectAttributes.addFlashAttribute("info","OK,更新成功!");
		return redirectTo("/system/setting/page.html");
		
    }
    
}
