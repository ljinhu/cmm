package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysScore;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.vo.SysScoreVo;

import java.util.List;

public interface SysScoreService extends IService<SysScore> {

    Page<SysScore> getScore(SysScoreVo score, Page<SysScore> page, String roleName);

    boolean save(SysScore sysScore, SysUser sysUser);

    List<SysScore> getAllScoreByExamCode(String code,String stuNo);
}
