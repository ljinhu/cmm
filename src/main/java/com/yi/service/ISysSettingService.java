package com.yi.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysSetting;

/**
 *
 * SysSetting 表数据服务层接口
 *
 */
public interface ISysSettingService extends IService<SysSetting> {

	List<SysSetting> findAll();


}