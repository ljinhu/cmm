package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.PhotoWall;
import com.yi.entity.vo.PhotoWallPoJo;
import com.yi.mapper.PhotoWallMapper;
import com.yi.service.PhotoWallService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/25 16:23
 * @Description: 
 */
@Service
public class PhotoWallServiceImpl extends ServiceImpl<PhotoWallMapper, PhotoWall> implements PhotoWallService {
    @Override
    public List<PhotoWall> findByClassId(String classId) {
        Wrapper<PhotoWall> wrapper = new EntityWrapper<>();
        wrapper.eq("class_id",classId);
        List<PhotoWall> photoWalls = this.selectList(wrapper);
        return photoWalls;
    }

    @Transactional
    @Override
    public boolean save(PhotoWallPoJo photoWallPoJo) {
        try {
            List<PhotoWall> photoWallList = photoWallPoJo.getPhotoWallList();
            if (!CollectionUtils.isEmpty(photoWallList)) {
                for (PhotoWall photoWall : photoWallList) {
                    this.insertOrUpdate(photoWall);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}