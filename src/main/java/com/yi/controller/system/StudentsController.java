package com.yi.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.service.ISysStudentsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:
 * @Date: 2020/2/28 15:08
 * @Description: 学生控制器
 */
@Controller
@RequestMapping("/system/student")
public class StudentsController extends BaseController {

    private final String PREFIX = "system/student/";
    @Autowired
    private ISysStudentsService studentsService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @param sysStudents
     * @param model
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       SysStudents sysStudents, Model model){
        Page<SysStudents> page = this.getPage(pageNo, pageSize);
        Page<SysStudents> pageRecord = studentsService.findByPage(sysStudents, page);
        model.addAttribute("pageRecord",pageRecord);
        return PREFIX + "list";
    }

    /**
     * 去添加页面
     * @param id 如果有id则说明是修改
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(String id,Model model){
        SysStudents sysStudents = new SysStudents();
        if(StringUtils.isNotEmpty(id)){
            sysStudents = this.studentsService.selectById(id);
        }
        model.addAttribute("bean",sysStudents);
        return PREFIX +"add";
    }

    /**
     * 执行添加
     * 业务上要求添加学生用户时，也要同时添加家长
     * 家长信息存放在sysuser表中，角色为家长
     * @param students
     * @param sysUser
     * @return
     */
    @RequestMapping("/doAdd")
    public Rest doAdd(SysStudents students, SysUser sysUser,String[] roleIds){
        return studentsService.save(students,sysUser,roleIds);
    }
}