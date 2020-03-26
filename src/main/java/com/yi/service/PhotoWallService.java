package com.yi.service;

import com.baomidou.mybatisplus.service.IService;
import com.yi.entity.PhotoWall;
import com.yi.entity.vo.PhotoWallPoJo;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/25 16:22
 * @Description: 
 */
public interface PhotoWallService extends IService<PhotoWall> {

    List<PhotoWall> findByClassId(String classId);

    boolean save(PhotoWallPoJo photoWallPoJo);
    boolean save(PhotoWall photoWall);
}