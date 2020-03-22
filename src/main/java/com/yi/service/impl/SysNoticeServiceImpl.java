package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.SysClass;
import com.yi.entity.SysNotice;
import com.yi.entity.SysUser;
import com.yi.mapper.SysNoticeMapper;
import com.yi.service.ISysClassService;
import com.yi.service.SysNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
    @Autowired
    private ISysClassService classService;

    @Override
    public Page<SysNotice> findPage(Page<SysNotice> page, SysNotice notice) {
        if (null != notice) {
            Wrapper<SysNotice> wrapper = new EntityWrapper<>();

            if (StringUtils.isNotEmpty(notice.getClassNo())) {
                wrapper.eq("class_no", notice.getClassNo());
            }

            if (StringUtils.isNotEmpty(notice.getCreatedBy())) {
                wrapper.and().eq("CREATED_BY", notice.getCreatedBy());
            }

            if (StringUtils.isNotEmpty(notice.getName())) {
                wrapper.and().like("name", notice.getName());
            }

            Page<SysNotice> noticePage = this.selectPage(page, wrapper);
            return noticePage;
        }
        return page;
    }

    @Override
    public boolean save(SysNotice sysNotice) {
        if (null == sysNotice) {
            return false;
        } else {
            try {
                if (StringUtils.isNotEmpty(sysNotice.getClassNo())) {
                    SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
                    sysNotice.setCreatedBy(currentUser.getId());
                    sysNotice.setCreatedName(currentUser.getUserName());
                    sysNotice.setCreatedTime(new Date());
                    //获取班级信息
                    SysClass sysClass = classService.getByClassNo(sysNotice.getClassNo());
                    sysNotice.setClassName(sysClass.getName());
                    //执行保存
                    this.insert(sysNotice);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
