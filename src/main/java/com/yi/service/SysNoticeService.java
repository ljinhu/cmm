package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysNotice;

import java.util.List;

public interface SysNoticeService extends IService<SysNotice> {

    Page<SysNotice> findPage(Page<SysNotice> page,SysNotice notice);

    boolean save(SysNotice sysNotice);

    Page<SysNotice> findByClassIds(Page<SysNotice> page,List<String> classNos);

}
