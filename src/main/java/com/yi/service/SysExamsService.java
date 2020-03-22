package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysExams;

public interface SysExamsService extends IService<SysExams> {

    /**
     * 根据角色名称获取用户可以看到的考试
     * @param exams
     * @param page
     * @param roleName
     * @return
     */
    Page<SysExams> mineExams(SysExams exams,Page<SysExams> page,String roleName);

    boolean save(SysExams sysExams);
}
