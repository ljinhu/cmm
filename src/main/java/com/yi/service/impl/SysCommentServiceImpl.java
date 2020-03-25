package com.yi.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.SysComment;
import com.yi.mapper.SysCommentMapper;
import com.yi.service.SysCommentService;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Date: 2020/3/24 16:43
 * @Description: 
 */
@Service
public class SysCommentServiceImpl extends ServiceImpl<SysCommentMapper, SysComment> implements SysCommentService {
    @Override
    public Page<SysComment> getByPage(Page<SysComment> page, SysComment comment, String roleName) {
        return null;
    }
}