package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.common.bean.Rest;
import com.yi.entity.SysClass;
import com.yi.entity.SysUser;
import com.yi.mapper.SysClassMapper;
import com.yi.mapper.SysUserMapper;
import com.yi.service.ISysClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/24 15:24
 * @Description:
 */
@Service
public class SysClassServiceImpl extends ServiceImpl<SysClassMapper, SysClass> implements ISysClassService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Override
    public Page<SysClass> findPage(SysClass sysClass, Page<SysClass> page) {
        Wrapper<SysClass> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(sysClass.getName())) {
            wrapper.like("name", sysClass.getName());
        }
        if (StringUtils.isNotEmpty(sysClass.getChargeUid())) {
            wrapper.and().eq("CHARGE_UID", sysClass.getChargeUid());
        }
        Page<SysClass> sysClassPage = this.selectPage(page, wrapper);
        return sysClassPage;
    }

    @Override
    @Transactional
    public Rest save(SysClass sysClass) {
        if (null == sysClass) {
            return Rest.failure("保存失败");
        }

        if (StringUtils.isEmpty(sysClass.getChargeUid())) {
            return Rest.failure("没有选择班主任，保存失败");
        }
        boolean result = false;
        //判断有没有id，有id说明时更新操作
        if(StringUtils.isNotEmpty(sysClass.getId())){
            result = this.updateById(sysClass);
        }else{
            //否则是插入
            result = this.insert(sysClass);
        }
        if (result) {
            return Rest.ok("保存成功");
        } else {
            return Rest.failure("保存失败");
        }
    }

    @Override
    public List<SysUser> selectUserByRole(String roleName) {
        return sysUserMapper.selectUserByRole(roleName);
    }
}