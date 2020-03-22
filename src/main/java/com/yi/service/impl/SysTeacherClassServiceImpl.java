package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.Dict;
import com.yi.entity.SysClass;
import com.yi.entity.SysTeacherClass;
import com.yi.entity.SysUser;
import com.yi.mapper.SysTeacherClassMapper;
import com.yi.service.DictService;
import com.yi.service.ISysClassService;
import com.yi.service.ISysUserService;
import com.yi.service.SysTeacherClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysTeacherClassServiceImpl extends ServiceImpl<SysTeacherClassMapper, SysTeacherClass> implements SysTeacherClassService {
    @Autowired
    private ISysClassService classService;
    @Autowired
    private DictService dictService;

    @Autowired
    private ISysUserService userService;

    @Override
    public List<SysClass> getTeacherClass(String teacherId, Long isValid) {
        //先查询关联表
        Wrapper<SysTeacherClass> sysTeacherClassWrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(teacherId)) {
            sysTeacherClassWrapper.eq("USER_ID", teacherId);
        }
        if (null != isValid) {
            sysTeacherClassWrapper.and().eq("IS_VALID", isValid);
        }
        List<SysTeacherClass> sysTeacherClasses = this.selectList(sysTeacherClassWrapper);
        if (!CollectionUtils.isEmpty(sysTeacherClasses)) {
            //获取classId的集合
            List<String> classIds = sysTeacherClasses.stream().map(sysTeacherClass -> sysTeacherClass.getClassId())
                    .collect(Collectors.toList());
            List<SysClass> classes = classService.selectBatchIds(classIds);
            return classes;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(SysTeacherClass sysTeacherClass) {
        //获取相关信息，用户、课程等
        if (StringUtils.isEmpty(sysTeacherClass.getClassId())) {
            return false;
        }
        if (StringUtils.isEmpty(sysTeacherClass.getLessonCode())) {
            return false;
        }
        List<Dict> lessons = dictService.selectList("lesson", sysTeacherClass.getLessonCode());
        if (!CollectionUtils.isEmpty(lessons)) {
            Dict dict = lessons.get(0);
            sysTeacherClass.setLessonName(dict.getName());
        }

        if (StringUtils.isNotEmpty(sysTeacherClass.getUserId())) {
            SysUser sysUser = userService.selectById(sysTeacherClass.getUserId());
            if (null == sysUser) {
                return false;
            }
        }
        try {
            sysTeacherClass.setIsValid(1L);
            this.insertOrUpdate(sysTeacherClass);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
