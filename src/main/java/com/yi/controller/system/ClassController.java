package com.yi.controller.system;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.*;
import com.yi.entity.vo.SysTeacherClassVo;
import com.yi.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Date: 2020/2/24 15:19
 * @Description:
 */
@Controller
@RequestMapping("/system/class")
public class ClassController extends BaseController {
    private final String PREFIX = "system/class/";
    @Autowired
    private ISysClassService sysClassService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private DictService dictService;
    @Autowired
    private DictTypeService dictTypeService;

    @Autowired
    private ISysStudentsService studentsService;
    @Autowired
    private SysTeacherClassService teacherClassService;

    /**
     * 分页查询班级
     *
     * @param pageNo   第几页
     * @param pageSize 每页显示的数据
     * @param name     班级名称
     * @param model
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Model model) {
        Page<SysClass> page = getPage(pageNo, pageSize);
        SysClass sysClass = new SysClass();
        sysClass.setName(name);
        Page<SysClass> sysClassPage = sysClassService.findPage(sysClass, page);
        model.addAttribute("pageData", sysClassPage);
        model.addAttribute("name", name);
        return PREFIX + "list";
    }

    /**
     * 跳转到添加添加或修改页面
     *
     * @return
     */
    @RequiresPermissions({"system:class:add"})
    @RequestMapping("/toAdd")
    public String toAdd(Model model, String id) {
        SysClass sysClass = new SysClass();
        if (StringUtils.isNotEmpty(id)) {
            //如果id不为空，去查询
            sysClass = sysClassService.selectById(id);
            //如果查到，说明时修改，需要把原数据传到表单上回显
            model.addAttribute("sysClass", sysClass);
        }
        //获取班主任列表，供添加时选择班主任
        List<SysUser> users = sysRoleService.getSysUsers("a1d79fef008c4e40bb3d4398e170ada1");
        model.addAttribute("charges", users);
        //获取年级
        List<Dict> grades = dictService.findByTypeCode("grade");
        model.addAttribute("grades", grades);
        return PREFIX + "add";
    }

    /**
     * 执行添加或修改
     *
     * @return
     */
    @RequiresPermissions({"system:class:add"})
    @RequestMapping("/add")
    @ResponseBody
    public Rest doAdd(SysClass sysClass) {
        return sysClassService.save(sysClass);
    }

    @RequestMapping("/delete")
    @RequiresPermissions("system:class:del")
    @ResponseBody
    public Rest delete(String id, Long isValid) {
        this.sysClassService.del(id, isValid);
        return Rest.ok("操作成功");
    }


    /**
     * 通过excel导入
     *
     * @return
     */
    @RequestMapping("/import")
    @ResponseBody
    public Rest importStu(@RequestParam MultipartFile file) {
        return sysClassService.importStuExcel(file);
    }

    /**
     * 根据不同角色展示班级信息
     *
     * @return
     */
    @RequestMapping("/myClass/{pageNo}")
    public String mineClass(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                            String name, Model model) {
        Page<SysClass> page = getPage(pageNo, pageSize);
        //先获取当前登录用户信息
        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if (isCharge()) {
            //是班主任
            Wrapper wrapper = new EntityWrapper<SysClass>();
            wrapper.eq("charge_uid", currentUser.getId());
            if (StringUtils.isNotEmpty(name)) {
                wrapper.and().eq("name", name);
            }
            Page classes = sysClassService.selectPage(page, wrapper);
            model.addAttribute("pageData", classes);
            return PREFIX + "list";
        } else if (isParent()) {
            //是家长
            return PREFIX + "mine";
        } else {
            List<SysClass> classes = teacherClassService.getTeacherClass(currentUser.getId(), 1L);
            page = new Page<>();
            page.setRecords(classes);
            page.setTotal(classes.size());
            model.addAttribute("pageData", page);
            //以上都不是，就默认教师
            return PREFIX + "list";
        }
    }

    /**
     * 班级学生管理
     *
     * @return
     */
    @RequestMapping("/manage/student/{id}/{pageNo}")
    public String manage(@PathVariable String id, @PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, Model model, String no) {
        SysClass sysClass = sysClassService.selectById(id);
        model.addAttribute("class", sysClass);
        Page<SysStudents> page = getPage(pageNo, pageSize);
        Wrapper<SysStudents> stuWrapper = new EntityWrapper<>();
        stuWrapper.eq("class_no", sysClass.getClassNo());
        if (StringUtils.isNotEmpty(no)) {
            stuWrapper.and().like("no", no);
        }
        model.addAttribute("no", no);
        Page<SysStudents> result = studentsService.selectPage(page, stuWrapper);
        model.addAttribute("pageData", result);
        return PREFIX + "stulist";
    }

    @RequestMapping("remove/student/{stuId}/{classNo}")
    @ResponseBody
    public Rest remove(@PathVariable String stuId, @PathVariable String classNo) {
        try {
            sysClassService.removeStudent(stuId, classNo);
        } catch (Exception e) {
            e.printStackTrace();
            return Rest.failure("移除失败，系统出现错误");
        }
        return Rest.ok("移除成功");
    }

    /**
     * 班级教师管理
     *
     * @param id
     * @return
     */
    @RequestMapping("manage/teacher/{id}")
    public String teacherManage(@PathVariable String id, Model model) {
        //获取课程字典
        List<Dict> lessons = dictService.findByTypeCode("lesson");
        Wrapper<SysTeacherClass> wrapper = new EntityWrapper<>();
        wrapper.eq("class_id", id);
        wrapper.and().eq("IS_VALID", 1);
        List<SysTeacherClass> sysTeacherClasses = teacherClassService.selectList(wrapper);
        if (!CollectionUtils.isEmpty(lessons)) {
            List<SysTeacherClassVo> voList = lessons.stream().map(lesson -> {
                SysTeacherClassVo vo = new SysTeacherClassVo();
//                BeanUtils.copyProperties(sysTeacherClasses, vo);
                vo.setLesson(lesson.getName());
                vo.setLessonCode(lesson.getValue());
                vo.setClassId(id);
                sysTeacherClasses.forEach(sysTeacherClass -> {
                    if (lesson.getValue().equals(sysTeacherClass.getLessonCode())) {
                        BeanUtils.copyProperties(sysTeacherClass,vo);
                        SysUser sysUser = userService.selectById(sysTeacherClass.getUserId());
                        if (null != sysUser) {
                            vo.setTeacherName(sysUser.getUserName());
                        }
                    }
                });
                return vo;
            }).collect(Collectors.toList());

            model.addAttribute("list", voList);
        }
        return PREFIX + "teacheList";
    }

    /**
     * 添加或修改
     * @param model
     * @param classId
     * @param lessonCode
     * @param id
     * @return
     */
    @RequestMapping("/toAddTeacher")
    public String toAddTeacher(Model model, String classId, String lessonCode,String id) {
        if(StringUtils.isNotEmpty(id)){
            //查询
            SysTeacherClass sysTeacherClass = teacherClassService.selectById(id);
            model.addAttribute("sysTeacherClass",sysTeacherClass);
        }
        List<Dict> lesson = dictService.selectList("lesson", lessonCode);
        if (!org.springframework.util.CollectionUtils.isEmpty(lesson)) {
            model.addAttribute("lesson",lesson.get(0));
        }
        model.addAttribute("classId", classId);
        model.addAttribute("lessonCode", lessonCode);
        //查询所用教师角色的用户
        List<SysUser> sysUsers = sysRoleService.getSysUsers("6d0a4b3f820b4b39aa158ff7cfc3001a");
        model.addAttribute("users", sysUsers);
        return PREFIX + "addTeacher";
    }

    @RequestMapping("/saveTeacher")
    @ResponseBody
    public Rest saveTeacher(SysTeacherClass sysTeacherClass){
        teacherClassService.saveOrUpdate(sysTeacherClass);
        return Rest.ok("保存成功");
    }
}