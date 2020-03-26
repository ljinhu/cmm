package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysNotice;
import com.yi.entity.SysUser;
import com.yi.service.SysNoticeService;
import com.yi.service.SysTeacherClassService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 首页控制器
* @ClassName: IndexController
* @author Gaojun.Zhou
* @date 2016年12月8日 下午8:42:40
*
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    @Autowired
    private SysNoticeService noticeService;
    @Autowired
    private SysTeacherClassService teacherClassService;
	
    @RequestMapping({"","/","index"})  
    public  String index(Model model){
        SysUser sysUser = cuurenUser();
        if(isCharge()){
            //是班主任

        }else if(isTeacher()){
            //查询班级公告
            //查询教师所在的所有班级



        }else if(isParent()){

        }
		return "index";
    }  
}
