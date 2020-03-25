package com.yi.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.*;
import com.yi.service.DictService;
import com.yi.service.ISysStudentsService;
import com.yi.service.SysCommentService;
import com.yi.service.SysTeacherClassService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/24 16:44
 * @Description:
 */
@Controller
@RequestMapping("/system/comment")
public class SysCommentController extends BaseController {

    private final String prefix = "/system/comment/";
    @Autowired
    private SysCommentService commentService;
    @Autowired
    private ISysStudentsService studentsService;
    @Autowired
    private SysTeacherClassService teacherClassService;
    @Autowired
    private DictService dictService;

    /**
     * @param model
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(Model model, @PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, SysComment sysComment) {
        //如果是家长，可以通过侧边栏直接打开，没有学生编号信息，所以要先查询
        //教师需要添加创建人的限制
        if (isParent()) {
            SysStudents student = studentsService.getByPid(cuurenUser().getId());
            if (null == student) {
                return prefix + "list";
            } else {
                sysComment.setStuNo(student.getNo());
            }
        } else if (isTeacher()) {
            sysComment.setCreateId(cuurenUser().getId());
        }
        Page<SysComment> page = this.commentService.getByPage(sysComment, getPage(pageNo, pageSize));
        model.addAttribute("pageData",page);
        return prefix + "list";
    }

    /**
     * 如果含有id就是修改
     *
     * @param model
     * @param stuId
     * @param id
     * @return
     */
    @RequestMapping("toAdd")
    @RequiresPermissions("system:comment:add")
    public String toAdd(Model model, String stuId, String id) {
        if (StringUtils.isNotEmpty(id)) {
            SysComment sysComment = commentService.selectById(id);
            model.addAttribute("sysComment", sysComment);
        }
        //获取对应的学生信息
        SysStudents student = studentsService.selectById(stuId);
        if (null == student) {
            return prefix + "add";
        }
        model.addAttribute("student", student);
        //获取所教课程供选择
        String classId = student.getClassId();
        Wrapper<SysTeacherClass> tcWrp = new EntityWrapper<>();
        tcWrp.eq("CLASS_ID", classId);
        tcWrp.eq("USER_ID", cuurenUser().getId());
        tcWrp.eq("IS_VALID", 1L);
        List<SysTeacherClass> lessons = teacherClassService.selectList(tcWrp);
        model.addAttribute("lessons", lessons);
        model.addAttribute("stuId", stuId);
        List<Dict> commentType = dictService.findByTypeCode("commentType");
        model.addAttribute("types",commentType);
        return prefix + "add";
    }

    @RequestMapping("/doAdd")
    @RequiresPermissions("system:comment:add")
    @ResponseBody
    public Rest doAdd(SysComment sysComment) {
        if (this.commentService.save(sysComment)) {
            return Rest.ok("保存成功");
        } else {
            return Rest.failure("保存失败");
        }
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public Rest delete(@PathVariable String id){
        try {
            this.commentService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return Rest.failure("删除失败");
        }
        return Rest.ok("删除成功");
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable String id,Model model){
        SysComment sysComment = this.commentService.selectById(id);
        model.addAttribute("sysComment",sysComment);
        return prefix +"detail";
    }
}