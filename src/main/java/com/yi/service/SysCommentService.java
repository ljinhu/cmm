package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.pojo.SysComment;

/**
 * @Author:
 * @Date: 2020/3/24 16:42
 * @Description: 
 */
public interface SysCommentService extends IService<SysComment> {

    Page<SysComment> getByPage(SysComment comment,Page<SysComment> page);

    boolean save(SysComment sysComment);
}