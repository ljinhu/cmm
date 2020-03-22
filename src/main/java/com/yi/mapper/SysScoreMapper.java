package com.yi.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.entity.SysScore;

import java.util.List;

/**
 * @Author: zwt
 * @Date: 2020/2/23 18:04
 * @Description: 
 */
public interface SysScoreMapper extends BaseMapper<SysScore> {

    List<SysScore> getScore(Page<SysScore> page, SysScore score);
}