package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysExams;
import com.yi.entity.vo.SysExamsVo;

public interface SysExamsService extends IService<SysExams> {

    /**
     * 根据角色名称获取用户可以看到的考试
     * @param exams
     * @param page
     * @param roleName
     * @return
     */
    Page<SysExams> mineExams(SysExams exams,Page<SysExams> page,String roleName);
    Page<SysExamsVo> mineExams(Page<SysExams> page, String roleName,SysExams exams);

    boolean save(SysExams sysExams);

    boolean delete(String id);

}
