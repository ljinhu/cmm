package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.common.bean.Rest;
import com.yi.entity.SysClass;
import com.yi.entity.SysUser;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/24 15:20
 * @Description: 
 */
public interface ISysClassService extends IService<SysClass> {

    Page<SysClass> findPage(SysClass sysClass,Page<SysClass> page);

    Rest save(SysClass sysClass);

    /**
     * 根据角色名称获取用户
     * @param roleName
     * @return
     */
    List<SysUser> selectUserByRole(String roleName);
}