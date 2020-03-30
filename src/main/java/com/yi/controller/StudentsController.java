package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.pojo.SysClass;
import com.yi.pojo.SysStudents;
import com.yi.pojo.SysUser;
import com.yi.pojo.vo.StudentVo;
import com.yi.service.ISysClassService;
import com.yi.service.ISysStudentsService;
import com.yi.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
    @Autowired
    private ISysClassService sysClassService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysClassService classService;

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
                       SysStudents sysStudents, Model model,String code){
        Page<SysStudents> page = this.getPage(pageNo, pageSize);
        List<SysClass> classes = null;
        if (isCharge()) {
            String id = cuurenUser().getId();
            Wrapper<SysClass> wrapper = new EntityWrapper();
            wrapper.eq("CHARGE_UID",id);
            classes = sysClassService.selectList(wrapper);
            if(CollectionUtils.isEmpty(classes)){
                SysClass sysClass = new SysClass();
                sysClass.setId("-1");
                classes.add(sysClass);
            }
        }
        Page<SysStudents> pageRecord = studentsService.findByPage(sysStudents, page,classes);
        model.addAttribute("pageData",pageRecord);
        model.addAttribute("no",sysStudents.getNo());
        model.addAttribute("code",code);
        return PREFIX + "list";
    }

    /**
     * 去添加页面
     * @param id 如果有id则说明是修改
     * @return
     */
    @RequiresPermissions("system:stu:add")
    @RequestMapping("/toAdd")
    public String toAdd(String id,Model model){
        SysStudents sysStudents = new SysStudents();
        if(StringUtils.isNotEmpty(id)){
            sysStudents = this.studentsService.selectById(id);
            //查询家长信息
            SysUser user = userService.selectById(sysStudents.getParentId());
            model.addAttribute("parent",user);
        }
        //获取所有班级供选择
        if(isCharge()){
            List<SysClass> classes = classService.getClassByChargeUid(cuurenUser().getId(), 1L);
            model.addAttribute("classes",classes);
        }else{
            List<SysClass> classes = classService.selectList(new EntityWrapper<>());
            model.addAttribute("classes",classes);
        }
        model.addAttribute("bean",sysStudents);
        return PREFIX +"add";
    }

    /**
     * 执行添加
     * 业务上要求添加学生用户时，也要同时添加家长
     * 家长信息存放在sysuser表中，角色为家长
     * @param student
     * @return
     */
    @RequiresPermissions("system:stu:add")
    @RequestMapping("/doAdd")
    @ResponseBody
    public Rest doAdd(StudentVo student){
        return studentsService.save(student);
    }

    /**
     * 检查学号是否重复
     * @param no
     * @return
     */
    @RequestMapping("/checkNo")
    @ResponseBody
    public Rest checkNo( String no){
        Rest rest = studentsService.checkNo(no);
        return rest;
    }

    @RequestMapping("/toimport")
    public String toimport(){
        return PREFIX +"import";
    }

    @RequestMapping("/import")
    @ResponseBody
    public Rest importExcel(@RequestParam MultipartFile file)throws Exception{

        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
       studentsService.importExcel(file);

        return Rest.ok();
    }

    @RequestMapping("/imp")
    @ResponseBody
    public Rest importExcel()throws Exception{
        return Rest.ok("上传完成");
    }

    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("system:stu:del")
    public Rest delete(String id){
        return studentsService.delete(id);
    }

}