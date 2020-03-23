package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.*;
import com.yi.mapper.SysExamsMapper;
import com.yi.service.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysExamsServiceImpl extends ServiceImpl<SysExamsMapper, SysExams> implements SysExamsService {
    @Autowired
    private ISysClassService classService;
    @Autowired
    private SysTeacherClassService teacherClassService;
    @Autowired
    private DictService dictService;
    @Autowired
    private ISysStudentsService sysStudentsService;
    @Autowired
    private SysScoreService sysScoreService;

    @Override
    public Page<SysExams> mineExams(SysExams exams, Page<SysExams> page, String roleName) {
        Wrapper<SysExams> wrapper = new EntityWrapper<>();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //存在班级id,则直接查询
        if (StringUtils.isEmpty(exams.getClassId())) {
            //班级id的集合，因为考试信息是和班级关联的
            List<String> classIds = new ArrayList<>();
            if ("manager".equals(roleName)) {
                //是班出任
                Wrapper<SysClass> classWrap = new EntityWrapper<>();
                classWrap.eq("charge_uid", user.getId());
                List<SysClass> classes = classService.selectList(classWrap);
                if (!CollectionUtils.isEmpty(classes)) {
                    //获取所有的班级集合
                    classIds = classes.stream().map(SysClass::getId).collect(Collectors.toList());
                }

            } else if ("teacher".equals(roleName)) {
                //是教师，去教师-班级关联表查找班级id
                List<SysClass> teacherClass = teacherClassService.getTeacherClass(user.getId(), 1L);
                if (!CollectionUtils.isEmpty(teacherClass)) {
                    classIds = teacherClass.stream().map(SysClass::getId).collect(Collectors.toList());
                }
                Wrapper<SysTeacherClass> stwp = new EntityWrapper<>();
                stwp.eq("user_id", user.getId());
                List<SysTeacherClass> sysTeacherClasses = teacherClassService.selectList(stwp);
                if (!CollectionUtils.isEmpty(sysTeacherClasses)) {
                    List<String> lessonCodes = sysTeacherClasses.stream().map(SysTeacherClass::getLessonCode).collect(Collectors.toList());
                    wrapper.in("lesson_Code", lessonCodes);
                }
            } else {
                //是家长，先查学生，可以获取到当前所在的班级id
                List<SysClass> classes = classService.getClassesByPid(user.getId(), 1L);
                if (!CollectionUtils.isEmpty(classes)) {
                    classIds = classes.stream().map(SysClass::getId).collect(Collectors.toList());
                }

            }
            if (!CollectionUtils.isEmpty(classIds)) {
                wrapper.in("class_id", classIds);
            }
        } else {
            if ("teacher".equals(roleName)) {
                Wrapper<SysTeacherClass> stwp = new EntityWrapper<>();
                stwp.eq("user_id", user.getId());
                List<SysTeacherClass> sysTeacherClasses = teacherClassService.selectList(stwp);
                if (!CollectionUtils.isEmpty(sysTeacherClasses)) {
                    List<String> lessonCodes = sysTeacherClasses.stream().map(SysTeacherClass::getLessonCode).collect(Collectors.toList());
                    wrapper.in("lesson_Code", lessonCodes);
                }
            }
            wrapper.in("class_id", exams.getClassId());
        }
        Page<SysExams> sysExamsPage = this.selectPage(page, wrapper);
        return sysExamsPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(SysExams sysExams) {
        if (!StringUtils.isEmpty(sysExams.getClassId())) {
            //获取所有学生信息
            Wrapper<SysStudents> stuWrapper = new EntityWrapper<>();
            stuWrapper.eq("CLASS_ID",sysExams.getClassId());
            List<SysStudents> sysStudents = sysStudentsService.selectList(stuWrapper);
            if ("all".equals(sysExams.getLessonCode())) {
                List<Dict> lessons = dictService.findByTypeCode("lesson");
                if (!CollectionUtils.isEmpty(lessons)) {
                    lessons.forEach(lesson -> {
                        SysExams exams = new SysExams();
                        BeanUtils.copyProperties(sysExams, exams);
                        //获取班级信息
                        SysClass sysClass = classService.selectById(exams.getClassId());
                        //设置班级名称
                        exams.setClassName(sysClass.getName());
                        exams.setLessonCode(lesson.getValue());
                        exams.setLessonName(lesson.getName());
                        exams.setGrade(sysClass.getGrade());
                        exams.setCreatedTime(new Date());
                        insertOrUpdate(exams);
                        //设置学生成绩都为空
                        if(!CollectionUtils.isEmpty(sysStudents)){
                            sysStudents.forEach(stu ->{
                                saveScore(lesson, exams, sysClass, stu);
                            });
                        }
                    });
                }
                return true;
            } else {
                List<Dict> lessons = dictService.selectList("lesson", sysExams.getLessonCode());
                if (!CollectionUtils.isEmpty(lessons)) {
                    final Dict lesson = lessons.get(0);
                    //获取班级信息
                    SysClass sysClass = classService.selectById(sysExams.getClassId());
                    sysExams.setLessonName(lesson.getName());
                    //设置班级名称
                    sysExams.setClassName(sysClass.getName());
                    sysExams.setGrade(sysClass.getGrade());
                    sysExams.setCreatedTime(new Date());
                    insertOrUpdate(sysExams);
                    //设置学生成绩都为空
                    if(!CollectionUtils.isEmpty(sysStudents)){
                        sysStudents.forEach(stu ->{
                            saveScore(lesson, sysExams, sysClass, stu);
                        });
                    }
                }
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(String id) {
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(id)){
            this.deleteById(id);
            //删除关联的成绩
            Wrapper<SysScore> scoreWrapper = new EntityWrapper<>();
            scoreWrapper.eq("exam_id",id);
            sysScoreService.delete(scoreWrapper);
        }
        return true;
    }

    private void saveScore(Dict lesson, SysExams exams, SysClass sysClass, SysStudents stu) {
        SysScore sysScore = new SysScore();
        sysScore.setExamId(exams.getId());
        sysScore.setClassId(stu.getClassId());
        sysScore.setClassName(sysClass.getName());
        sysScore.setExamTitle(exams.getTitle());
        sysScore.setLessonCode(lesson.getValue());
        sysScore.setLessonName(lesson.getName());
        sysScore.setStuNo(stu.getNo());
        sysScore.setStuName(stu.getName());
        sysScore.setParentId(stu.getParentId());
        sysScoreService.insert(sysScore);
    }
}
