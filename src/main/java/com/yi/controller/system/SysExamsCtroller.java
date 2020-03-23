package com.yi.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.Dict;
import com.yi.entity.SysClass;
import com.yi.entity.SysExams;
import com.yi.service.DictService;
import com.yi.service.ISysClassService;
import com.yi.service.SysExamsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


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
        model.addAttribute("classNo", classId);
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
}
