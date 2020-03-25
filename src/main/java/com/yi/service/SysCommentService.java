package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.SysComment;

/**
 * @Author:
 * @Date: 2020/3/24 16:42
 * @Description: 
 */
public interface SysCommentService extends IService<SysComment> {

    /**
     * 查看学生表现
     * @param page
     * @param comment
     * @param roleName
     * @return
     */
    Page<SysComment> getByPage(Page<SysComment> page,SysComment comment,String roleName);
}