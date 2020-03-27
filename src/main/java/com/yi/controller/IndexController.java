package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysClass;
import com.yi.entity.SysNotice;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.vo.SysClassVo;
import com.yi.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


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
    @Autowired
    private ISysClassService classService;
    @Autowired
    private ISysStudentsService studentsService;
    @Autowired
    private ISysRoleService roleService;

    @RequestMapping({"","/","index"})
    public  String index(Model model){
        SysUser sysUser = cuurenUser();
        if(isCharge()){
            //是班主任
            //管理的班级
            List<SysClass> classes = classService.getClassByChargeUid(cuurenUser().getId(), 1l);
            model.addAttribute("classes",classes);
            //管理的班级的公告数
            if(!StringUtils.isEmpty(classes)){
                List<String> classNos = classes.stream().map(SysClass::getClassNo).collect(Collectors.toList());
                Page<SysNotice> noticePage = noticeService.findByClassIds(getPage(1, 10), classNos);
                model.addAttribute("noticePage",noticePage);
            }
            model.addAttribute("role","charge");
        }else if(isTeacher()){
            //查询教师所在的所有班级
            List<SysClass> classes = teacherClassService.getTeacherClass(cuurenUser().getId(), 1L);
            model.addAttribute("classes",classes);
            //查询班级公告
            if(!StringUtils.isEmpty(classes)){
                List<String> classNos = classes.stream().map(SysClass::getClassNo).collect(Collectors.toList());
                Page<SysNotice> noticePage = noticeService.findByClassIds(getPage(1, 10), classNos);
                model.addAttribute("noticePage",noticePage);
            }
            model.addAttribute("role","teacher");
        }else if(isParent()){
            SysStudents students = studentsService.getByPid(cuurenUser().getId());
            SysClassVo detail = classService.detail(students.getClassId());
            model.addAttribute("detail",detail);
            Page<SysNotice> page = getPage(1, 10);
            SysNotice sysNotice = new SysNotice();
            sysNotice.setClassNo(students.getClassId());
            Page<SysNotice> noticePage = noticeService.findPage(page, sysNotice);
            model.addAttribute("noticePage",noticePage);
            model.addAttribute("role","parent");
        }else{
            //管理员
            //学生数
            Wrapper<SysStudents> stuWrp = new EntityWrapper<>();
            List<SysStudents> sysStudents = studentsService.selectList(stuWrp);
            model.addAttribute("students",sysStudents);
            //教师数
            List<SysUser> teachers = roleService.getSysUsers(teach_id);
            model.addAttribute("teachers",teachers);
            //班主任数
            List<SysUser> charges = roleService.getSysUsers(charge_id);
            model.addAttribute("charges",charges);
            //获取所有公告
            Page<SysNotice> noticePage = noticeService.findByClassIds(getPage(1, 10), null);
            model.addAttribute("noticePage",noticePage);
            model.addAttribute("role","admin");
        }
        return "index";
    }
}
