package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.pojo.SysHomeWork;

import java.util.List;

public interface SysHomeWorkService extends IService<SysHomeWork> {

    Page<SysHomeWork> getPageList(SysHomeWork work, Page<SysHomeWork> page, List<String> classIds);

    boolean save(SysHomeWork work) throws Exception;
}
