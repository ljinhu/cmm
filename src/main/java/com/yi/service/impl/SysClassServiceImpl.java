package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.common.bean.Rest;
import com.yi.common.util.ImportExcelUtil;
import com.yi.entity.SysClass;
import com.yi.entity.SysStudentClass;
import com.yi.entity.SysTeacherClass;
import com.yi.entity.SysUser;
import com.yi.mapper.SysClassMapper;
import com.yi.mapper.SysStudentClassMapper;
import com.yi.mapper.SysTeacherClassMapper;
import com.yi.mapper.SysUserMapper;
import com.yi.service.ISysClassService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/24 15:24
 * @Description:
 */
@Service
public class SysClassServiceImpl extends ServiceImpl<SysClassMapper, SysClass> implements ISysClassService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysTeacherClassMapper sysTeacherClassMapper;
    @Autowired
    private SysStudentClassMapper studentClassMapper;
    @Override
    public Page<SysClass> findPage(SysClass sysClass, Page<SysClass> page) {
        Wrapper<SysClass> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(sysClass.getName())) {
            wrapper.like("name", sysClass.getName());
        }
        if (StringUtils.isNotEmpty(sysClass.getChargeUid())) {
            wrapper.and().eq("CHARGE_UID", sysClass.getChargeUid());
        }
        wrapper.orderBy("CREATED_TIME",false);
        Page<SysClass> sysClassPage = this.selectPage(page, wrapper);
        return sysClassPage;
    }

    @Override
    @Transactional
    public Rest save(SysClass sysClass) {
        if (null == sysClass) {
            return Rest.failure("保存失败");
        }

        if (StringUtils.isEmpty(sysClass.getChargeUid())) {
            return Rest.failure("没有选择班主任，保存失败");
        }
        boolean result = false;
        //设置班主任姓名
        SysUser sysUser = sysUserMapper.selectById(sysClass.getChargeUid());
        sysClass.setChargeUname(sysUser.getUserName());
        //判断有没有id，有id说明时更新操作
        if(StringUtils.isNotEmpty(sysClass.getId())){
            SysClass oldClass = this.selectById(sysClass.getId());
            sysClass.setCreatedTime(oldClass.getCreatedTime());
            sysClass.setCreatedBy(oldClass.getCreatedBy());
            result = this.updateById(sysClass);
        }else{
            //设置创建时间，创建人
            SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysClass.setCreatedBy(currentUser.getUserName());
            sysClass.setCreatedTime(new Date());
            //否则是插入
            result = this.insert(sysClass);
        }
        if (result) {
            return Rest.ok("保存成功");
        } else {
            return Rest.failure("保存失败");
        }
    }

    @Override
    public List<SysUser> selectUserByRole(String roleName) {
        return sysUserMapper.selectUserByRole(roleName);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(String id,Long isValid) {
        SysClass sysClass = this.selectById(id);
        sysClass.setIsValid(isValid);
        //同时设置教师-班级关联关系状态
        Wrapper<SysTeacherClass> teacherClassWrapper = new EntityWrapper<>();
        teacherClassWrapper.eq("CLASS_ID",id);
        List<SysTeacherClass> sysTeacherClasses = sysTeacherClassMapper.selectList(teacherClassWrapper);
        if(!CollectionUtils.isEmpty(sysTeacherClasses)){
            for (SysTeacherClass teacherClass : sysTeacherClasses){
                //设置
                teacherClass.setIsValid(isValid);
                //更新
                sysTeacherClassMapper.updateById(teacherClass);
            }
        }
        //更新学生-班级关联表 状态改变
        Wrapper<SysStudentClass> studentClassWrapper = new EntityWrapper<>();
        studentClassWrapper.eq("CLASS_ID",id);
        List<SysStudentClass> sysStudentClasses = studentClassMapper.selectList(studentClassWrapper);
        if(!CollectionUtils.isEmpty(sysStudentClasses)){
            for(SysStudentClass sysStudentClass : sysStudentClasses){
                sysStudentClass.setIsValid(isValid);
                studentClassMapper.updateById(sysStudentClass);
            }
        }
        this.updateById(sysClass);
    }

    @Override
    public Rest importStuExcel(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        try {
            List<List<Object>> list = ImportExcelUtil.getBankListByExcel(file.getInputStream(), originalFilename);
        } catch (Exception e) {
            e.printStackTrace();
            Rest rest = Rest.failure("导入发生异常");
            return rest;
        }
        return Rest.ok();
    }
}