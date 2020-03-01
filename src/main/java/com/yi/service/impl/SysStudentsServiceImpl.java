package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.common.bean.Rest;
import com.yi.common.util.ShiroUtil;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.SysUserRole;
import com.yi.mapper.SysStudentsMapper;
import com.yi.mapper.SysUserMapper;
import com.yi.mapper.SysUserRoleMapper;
import com.yi.service.ISysStudentsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author:
 * @Date: 2020/2/28 15:16
 * @Description:
 */
@Service
public class SysStudentsServiceImpl extends ServiceImpl<SysStudentsMapper, SysStudents> implements ISysStudentsService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public Page<SysStudents> findByPage(SysStudents sysStudents, Page<SysStudents> page) {
        //构建查询条件
        Wrapper<SysStudents> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(sysStudents.getName())) {
            wrapper.like("name", sysStudents.getName());
        }

        if (StringUtils.isNotEmpty(sysStudents.getClassId())) {
            wrapper.and().eq("CLASS_ID", sysStudents.getClassId());
        }

        //执行查询
        Page<SysStudents> sysStudentsPage = this.selectPage(page, wrapper);
        return sysStudentsPage;
    }

    @Transactional
    @Override
    public Rest save(SysStudents sysStudents, SysUser sysUser, String[] roleIds) {
        //如果sysStudents含有id，时修改，不再保存user即家长信息了
        if(!StringUtils.isNotEmpty(sysStudents.getId())){
            sysUser.setCreateTime(new Date());
            sysUser.setPassword(ShiroUtil.md51024Pwd(sysUser.getPassword(), sysUser.getUserName()));
            //保存用户
            userMapper.insert(sysUser);
            //绑定角色
            if (ArrayUtils.isNotEmpty(roleIds)) {
                for (String rid : roleIds) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(sysUser.getId());
                    sysUserRole.setRoleId(rid);
                    userRoleMapper.insert(sysUserRole);
                }
            }
            sysStudents.setParentId(sysUser.getId());
            sysStudents.setParentName(sysUser.getUserName());
        }
        this.insertOrUpdate(sysStudents);
        return Rest.ok("保存成功");
    }
}