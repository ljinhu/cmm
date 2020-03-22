package com.yi.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.common.util.ImportExcelUtil;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.vo.StudentVo;
import com.yi.service.ISysStudentsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
        model.addAttribute("pageData",pageRecord);
        model.addAttribute("no",sysStudents.getNo());
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