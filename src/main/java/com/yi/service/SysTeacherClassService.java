package com.yi.service;

import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysClass;
import com.yi.entity.SysTeacherClass;

import java.util.List;

public interface SysTeacherClassService extends IService<SysTeacherClass> {

    /**
     * 根据teacherid查询教师所教的所有班级
     * @param teacherId
     * @return
     */
    List<SysClass> getTeacherClass(String teacherId,Long isValid);

    /**
     * 保存
     */
    boolean saveOrUpdate(SysTeacherClass sysTeacherClass);
}
