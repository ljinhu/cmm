package com.yi.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.pojo.SysClass;
import com.yi.pojo.SysHomeWork;
import com.yi.pojo.SysTeacherClass;
import com.yi.pojo.SysUser;
import com.yi.service.ISysClassService;
import com.yi.service.ISysStudentsService;
import com.yi.service.SysHomeWorkService;
import com.yi.service.SysTeacherClassService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/system/homework")
public class SysHomeWorkController extends BaseController {

    private final String prefix = "system/work/";

    @Autowired
    private SysHomeWorkService homeWorkService;
    @Autowired
    private ISysStudentsService studentsService;
    @Autowired
    private ISysClassService classService;
    @Autowired
    private SysTeacherClassService teacherClassService;

    /**
     * 分页获取
     *
     * @param model
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(Model model, @PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, SysHomeWork sysHomeWork) {
        Page<SysHomeWork> page = getPage(pageNo, pageSize);
        List<String> classIds = new ArrayList<>();
        List<SysClass> classes = new ArrayList<>(0);
        if (isCharge()) {
          classes  = classService.getClassByChargeUid(cuurenUser().getId(), 1L);
        }
        if (isParent()) {
             classes = classService.getClassesByPid(cuurenUser().getId(), 1L);
        }
        if (isTeacher()) {
            classes = teacherClassService.getTeacherClass(cuurenUser().getId(), 1L);
            sysHomeWork.setCreatedBy(cuurenUser().getId());
        }
        if(!CollectionUtils.isEmpty(classes)){
            classIds = classes.stream().map(SysClass::getId).collect(Collectors.toList());
        }
        Page<SysHomeWork> pageList = homeWorkService.getPageList(sysHomeWork, page, classIds);
        model.addAttribute("work",sysHomeWork);
        model.addAttribute("pageData",pageList);
        return prefix + "list";
    }

    @RequestMapping("/toAdd")
    @RequiresPermissions("system:work:add")
    public String toAdd(Model model){
        SysUser sysUser = cuurenUser();
        //如果教师教多门课程，需要获取
        Wrapper<SysTeacherClass> wrapper = new EntityWrapper<>();
        wrapper.eq("USER_ID",sysUser.getId());
        wrapper.and().eq("IS_VALID",1L);
        List<SysTeacherClass> sysTeacherClasses = teacherClassService.selectList(wrapper);
        model.addAttribute("classes",sysTeacherClasses);
        return prefix + "add";
    }
    @RequestMapping("/add")
    @ResponseBody
    public Rest doAdd(SysHomeWork work) {
        try {
            work.setCreatedBy(cuurenUser().getId());
            work.setCreateName(cuurenUser().getUserName());
            homeWorkService.save(work);
        }catch (Exception e){
            e.printStackTrace();
            return Rest.failure("发布失败");
        }
        return Rest.ok("发布成功");
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id,Model model){
        SysHomeWork sysHomeWork = homeWorkService.selectById(id);
        model.addAttribute("work",sysHomeWork);
        return prefix+"detail";
    }
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Rest delete(@PathVariable String id){
        try {
            homeWorkService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return Rest.failure("删除失败");
        }
        return Rest.ok();
    }

}
