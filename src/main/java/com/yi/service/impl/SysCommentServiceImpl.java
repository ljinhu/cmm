package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.*;
import com.yi.mapper.SysCommentMapper;
import com.yi.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/24 16:43
 * @Description: 
 */
@Service
public class SysCommentServiceImpl extends ServiceImpl<SysCommentMapper, SysComment> implements SysCommentService {
    @Autowired
    private ISysStudentsService studentsService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SysTeacherClassService teacherClassService;
    @Autowired
    private ISysClassService classService;
    @Override
    public Page<SysComment> getByPage(SysComment comment,Page<SysComment> page) {
        Wrapper<SysComment> wrapper = new EntityWrapper<>();
        if(StringUtils.isNotEmpty(comment.getClassId())){
            wrapper.eq("class_id",comment.getClassId());
        }
        if(StringUtils.isNotEmpty(comment.getStuNo())){
            wrapper.eq("stu_no",comment.getStuNo());
        }
        if(StringUtils.isNotEmpty(comment.getCreateId())){
            wrapper.eq("create_id",comment.getCreateId());
        }
        return this.selectPage(page,wrapper);
    }

    @Override
    public boolean save(SysComment sysComment) {
        //classId是教师-班级关联表的id
        try {
            String classId = sysComment.getClassId();
            SysTeacherClass teacherClass = teacherClassService.selectById(classId);
            sysComment.setLessonCode(teacherClass.getLessonCode());
            sysComment.setLessonName(teacherClass.getLessonName());
            sysComment.setClassId(teacherClass.getClassId());
            SysClass sysClass = classService.selectById(teacherClass.getClassId());
            if (null == sysClass) {
                return false;
            }
            sysComment.setClassName(sysClass.getName());
            SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysComment.setCreateId(user.getId());
            sysComment.setCreatedTime(new Date());
            sysComment.setCreatedName(user.getUserName());
            SysStudents students = studentsService.getByNo(sysComment.getStuNo());
            sysComment.setStuName(students.getName());
            List<Dict> commentType = dictService.selectList("commentType", sysComment.getType());
            if(!CollectionUtils.isEmpty(commentType)){
                sysComment.setType(commentType.get(0).getName());
            }
            this.insertOrUpdate(sysComment);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}