package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.common.bean.Rest;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;

/**
 * @Author:
 * @Date: 2020/2/28 15:14
 * @Description: 
 */
public interface ISysStudentsService extends IService<SysStudents> {

    /**
     * 按条件分页查询
     * @param sysStudents
     * @param page
     * @return
     */
    Page<SysStudents> findByPage(SysStudents sysStudents,Page<SysStudents> page);

    Rest save(SysStudents sysStudents, SysUser sysUser,String[] roleIds);
}