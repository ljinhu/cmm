package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.yi.entity.SysUser;
import com.yi.entity.SysUserRole;
import com.yi.service.ISysUserRoleService;
import com.yi.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.SysRole;
import com.yi.mapper.SysRoleMapper;
import com.yi.service.ISysRoleService;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * SysRole 表数据服务层接口实现类
 *
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserService sysUserService;

    @Override
    public List<SysUser> getSysUsers(String roleId) {
        List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("roleId", roleId));

        List<String> userIds = Lists.transform(sysUserRoles, input -> input.getUserId());

        List<SysUser> users  = new ArrayList<SysUser>();

        if(userIds.size() > 0){
            EntityWrapper<SysUser> ew = new EntityWrapper<SysUser>();
            ew.in("id", userIds);
            users= sysUserService.selectList(ew);
        }
        return users;
    }
}