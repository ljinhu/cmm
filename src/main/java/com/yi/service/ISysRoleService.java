package com.yi.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.google.common.collect.Lists;
import com.yi.entity.SysRole;
import com.yi.entity.SysUser;
import com.yi.entity.SysUserRole;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * SysRole 表数据服务层接口
 *
 */
public interface ISysRoleService extends IService<SysRole> {

     List<SysUser> getSysUsers(String roleId);
}