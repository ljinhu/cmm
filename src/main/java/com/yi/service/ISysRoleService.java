package com.yi.service;

import com.baomidou.mybatisplus.service.IService;
import com.yi.pojo.SysRole;
import com.yi.pojo.SysUser;

import java.util.List;

/**
 *
 * SysRole 表数据服务层接口
 *
 */
public interface ISysRoleService extends IService<SysRole> {

     List<SysUser> getSysUsers(String roleId);
}