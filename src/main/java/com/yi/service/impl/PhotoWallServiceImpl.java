package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.pojo.PhotoWall;
import com.yi.pojo.vo.PhotoWallPoJo;
import com.yi.dao.PhotoWallMapper;
import com.yi.service.PhotoWallService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        wrapper.eq("class_id", classId);
        List<PhotoWall> photoWalls = this.selectList(wrapper);
        return photoWalls;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PhotoWallPoJo photoWallPoJo) {
        try {
            List<PhotoWall> photoWallList = photoWallPoJo.getPhotoWallList();
            if (!CollectionUtils.isEmpty(photoWallList)) {
                for (PhotoWall photoWall : photoWallList) {
                    this.insertOrUpdate(photoWall);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(PhotoWall photoWall) {
        if (!StringUtils.isEmpty(photoWall.getClassId())) {
            String classId = photoWall.getClassId();
            if (!StringUtils.isEmpty(photoWall.getIndexOrder())) {
                String index = photoWall.getIndexOrder();
                Wrapper<PhotoWall> wrapper = new EntityWrapper<>();
                wrapper.eq("index_order", index);
                wrapper.and().eq("class_id", classId);
                PhotoWall photoWallDb = this.selectOne(wrapper);
                if (null != photoWallDb) {
                    photoWall.setId(photoWallDb.getId());
                }
            }
            this.insertOrUpdate(photoWall);
            return true;
        }
        return false;
    }
}