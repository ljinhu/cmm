package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.RoleNames;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.pojo.*;
import com.yi.pojo.vo.SysExamsVo;
import com.yi.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/system/exam")
public class SysExamsCtroller extends BaseController {
    private final String prefix = "system/exam/";
    @Autowired
    private SysExamsService examsService;
    @Autowired
    private ISysClassService classService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SysScoreService sysScoreService;
    @Autowired
    private ISysStudentsService studentsService;

    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Model model, String classId) {
        Page<SysExams> page = getPage(pageNo, pageSize);
        SysExams sysExams = new SysExams();
        sysExams.setTitle(name);
        sysExams.setClassId(classId);
        String role = "";
        if (isCharge()) {
            role = "manager";
        } else if (isTeacher()) {
            role = "teacher";
        } else {
            role = "parent";
        }
        Page<SysExams> sysExamsPage = examsService.mineExams(sysExams, page, role);
        model.addAttribute("pageData", sysExamsPage);
        model.addAttribute("name", name);
        model.addAttribute("classId", classId);
        return prefix + "list";
    }

    /**
     * 班主任功能，添加考试
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model, String id) {
        if (StringUtils.isNotEmpty(id)) {
            SysExams sysExams = examsService.selectById(id);
            model.addAttribute("sysExams", sysExams);
        }
        //获取班级
        Wrapper<SysClass> sysClassWrapper = new EntityWrapper<>();
        sysClassWrapper.eq("CHARGE_UID", cuurenUser().getId());
        List<SysClass> classes = classService.selectList(sysClassWrapper);
        model.addAttribute("classes", classes);
        List<Dict> lessons = dictService.findByTypeCode("lesson");
        model.addAttribute("lessons", lessons);
        return prefix + "add";
    }

    @RequestMapping("/doAdd")
    @ResponseBody
    public Rest doAdd(SysExams exams, String daterange) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String[] range = daterange.split("--");
        try {
            //设置申请时间
            Date begin = dateFormat.parse(range[0]);
            Date end = dateFormat.parse(range[1]);
            exams.setBegin(begin);
            exams.setEnd(end);
            String code = UUID.randomUUID().toString();
            exams.setCode(code);
            exams.setCreatedId(cuurenUser().getId());
            exams.setCreateName(cuurenUser().getUserName());
            if (examsService.save(exams)) {
                return Rest.ok("保存成功");
            } else {
                return Rest.failure("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Rest.failure("创建失败");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Rest delete(String examId) {
        try {
            boolean delete = this.examsService.delete(examId);
            if (delete) {
                return Rest.ok("删除成功");
            } else {
                return Rest.failure("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Rest.failure("删除失败");
        }
    }

    /**
     * 考试汇总
     * @param pageNo
     * @param pageSize
     * @param name
     * @param model
     * @return
     */
    @RequestMapping("/collect/{pageNo}")
    public String mineCollect(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                              String name, Model model){
        Page<SysExams> page = getPage(pageNo, pageSize);
        SysExams sysExams = new SysExams();
        sysExams.setTitle(name);
        String role = "";
        if (isCharge()) {
            role = RoleNames.MANAGER.getName();
            model.addAttribute("role",role);
        } else if (isTeacher()) {
            role = RoleNames.TEACHER.getName();
        } else {
            role = RoleNames.PARENT.getName();
            model.addAttribute("role",role);
        }
        Page<SysExamsVo> sysExamsPage = examsService.mineExams( page, role,sysExams);
        model.addAttribute("pageData", sysExamsPage);
        return prefix + "collect";
    }

    @RequestMapping("/detailList")
    public String detailList(String code,String stuNo,Model model){
        if(StringUtils.isEmpty(stuNo) && isParent()){
            //如果没有学号信息又是家长
            SysStudents students = studentsService.getByPid(cuurenUser().getId());
            if(students != null){
                stuNo = students.getNo();
            }
        }
        List<SysScore> scores = sysScoreService.getAllScoreByExamCode(code,stuNo);
        model.addAttribute("scores",scores);
        return prefix + "detailList";
    }
    @Autowired
    private ISysClassService sysClassService;
    @RequestMapping("/stulist/{pageNo}")
    public String stulist(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       SysStudents sysStudents, Model model, String code,String classId){
        Page<SysStudents> page = this.getPage(pageNo, pageSize);
        List<SysClass> classes = null;
        if (isCharge()) {
            String id = cuurenUser().getId();
            Wrapper<SysClass> wrapper = new EntityWrapper();
            wrapper.eq("CHARGE_UID",id);
            wrapper.eq("id",classId);
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
        model.addAttribute("classId",classId);
        return prefix + "stulist";
    }
}
