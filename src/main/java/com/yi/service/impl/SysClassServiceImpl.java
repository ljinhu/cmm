package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.common.bean.Rest;
import com.yi.common.util.ImportExcelUtil;
import com.yi.entity.*;
import com.yi.entity.vo.SysClassVo;
import com.yi.mapper.SysClassMapper;
import com.yi.mapper.SysStudentClassMapper;
import com.yi.mapper.SysTeacherClassMapper;
import com.yi.mapper.SysUserMapper;
import com.yi.service.ISysClassService;
import com.yi.service.ISysStudentsService;
import com.yi.service.PhotoWallService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private PhotoWallService photoWallService;
    @Autowired
    private ISysStudentsService sysStudentsService;
    @Autowired
    private ISysClassService classService;

    @Override
    public Page<SysClass> findPage(SysClass sysClass, Page<SysClass> page) {
        Wrapper<SysClass> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(sysClass.getName())) {
            wrapper.like("name", sysClass.getName());
        }
        if (StringUtils.isNotEmpty(sysClass.getChargeUid())) {
            wrapper.and().eq("CHARGE_UID", sysClass.getChargeUid());
        }
        wrapper.orderBy("CREATED_TIME", false);
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
        if (StringUtils.isNotEmpty(sysClass.getId())) {
            SysClass oldClass = this.selectById(sysClass.getId());
            sysClass.setCreatedTime(oldClass.getCreatedTime());
            sysClass.setCreatedBy(oldClass.getCreatedBy());
            result = this.updateById(sysClass);
        } else {
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
    public void del(String id, Long isValid) {
        SysClass sysClass = this.selectById(id);
        sysClass.setIsValid(isValid);
        //同时设置教师-班级关联关系状态
        Wrapper<SysTeacherClass> teacherClassWrapper = new EntityWrapper<>();
        teacherClassWrapper.eq("CLASS_ID", id);
        List<SysTeacherClass> sysTeacherClasses = sysTeacherClassMapper.selectList(teacherClassWrapper);
        if (!CollectionUtils.isEmpty(sysTeacherClasses)) {
            for (SysTeacherClass teacherClass : sysTeacherClasses) {
                //设置
                teacherClass.setIsValid(isValid);
                //更新
                sysTeacherClassMapper.updateById(teacherClass);
            }
        }
        //更新学生-班级关联表 状态改变
        Wrapper<SysStudentClass> studentClassWrapper = new EntityWrapper<>();
        studentClassWrapper.eq("CLASS_ID", id);
        List<SysStudentClass> sysStudentClasses = studentClassMapper.selectList(studentClassWrapper);
        if (!CollectionUtils.isEmpty(sysStudentClasses)) {
            for (SysStudentClass sysStudentClass : sysStudentClasses) {
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

    @Autowired
    private ISysStudentsService studentsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeStudent(String stuId, String classNo) throws Exception {
        Wrapper<SysClass> classWrapper = new EntityWrapper<>();
        classWrapper.eq("class_no", classNo);
        SysClass sysClass = this.selectOne(classWrapper);

        Wrapper<SysStudents> studentsWrapper = new EntityWrapper<>();
        studentsWrapper.eq("id", stuId);
        studentsWrapper.and().eq("CLASS_ID", classNo);
        SysStudents sysStudents = studentsService.selectOne(studentsWrapper);
        sysStudents.setClassId("");
        sysStudents.setChargeUname("");
        sysStudents.setClassName("");
        studentsService.updateById(sysStudents);
        //去除关联关系
//        Wrapper<SysStudentClass> scWrapper = new EntityWrapper<>();
//        scWrapper.eq("STU_ID",stuId);
//        scWrapper.and().eq("CLASS_ID",sysClass.getId());
        SysStudentClass sysStudentClass = new SysStudentClass();
        sysStudentClass.setStuId(stuId);
        sysStudentClass.setClassId(sysClass.getId());
        SysStudentClass studentClassDb = studentClassMapper.selectOne(sysStudentClass);
        studentClassDb.setIsValid(-1L);
        //设置关联关系失效
        studentClassMapper.updateById(studentClassDb);
        return true;
    }

    @Override
    public SysClass getByClassNo(String classNo) {
        if (StringUtils.isNotEmpty(classNo)) {
            Wrapper<SysClass> wrapper = new EntityWrapper<>();
            wrapper.eq("CLASS_NO", classNo);
            SysClass sysClass = this.selectOne(wrapper);
            return sysClass;
        }
        return null;
    }

    @Override
    public List<SysClass> getClassesByPid(String pid, Long isValid) {
        if (StringUtils.isNotEmpty(pid)) {
            //先查对应的学生
            Wrapper<SysStudents> wrapper = new EntityWrapper<>();
            wrapper.eq("PARENT_ID", pid);
            SysStudents student = studentsService.selectOne(wrapper);

            if (null != student) {
                //去查学生-班级关联表
                Wrapper<SysStudentClass> scWrp = new EntityWrapper<>();
                scWrp.eq("STU_ID", student.getId());
                if (null != isValid) {
                    scWrp.and().eq("IS_VALID", isValid);
                }
                List<SysStudentClass> studentClassList = studentClassMapper.selectList(scWrp);

                if (!CollectionUtils.isEmpty(studentClassList)) {
                    //获取所有的班级id
                    List<String> collect = studentClassList.stream().map(SysStudentClass::getClassId).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        //去查班级
                        Wrapper<SysClass> classWrapper = new EntityWrapper<>();
                        classWrapper.in("id", collect);
                        List<SysClass> classes = this.selectList(classWrapper);
                        return classes;
                    }
                }else{
                    //直接根据学生表的id去查
                    SysClass sysClass = classService.selectById(student.getClassId());
                    List<SysClass> classes = new ArrayList<>();
                    classes.add(sysClass);
                    return classes;
                }
            }
        }

        return null;
    }

    @Override
    public List<SysClass> getClassByChargeUid(String pid, Long isValid) {
        Wrapper<SysClass> wrapper = new EntityWrapper<>();
        wrapper.eq("CHARGE_UID", pid);
        if (null != isValid) {
            wrapper.and().eq("IS_VALID", isValid);
        }
        return this.selectList(wrapper);
    }

    @Override
    public SysClassVo detail(String id) {
        SysClassVo sysClassVo = new SysClassVo();
        //先查询基本信息
        SysClass sysClass = this.selectById(id);
        BeanUtils.copyProperties(sysClass, sysClassVo);
        //查询照片
        List<PhotoWall> photoWalls = photoWallService.findByClassId(sysClass.getId());
        sysClassVo.setPhotos(photoWalls);
        //查询学生数
        Wrapper<SysStudents> stuWrp = new EntityWrapper<>();
        stuWrp.eq("CLASS_ID", id);
        int stus = sysStudentsService.selectCount(stuWrp);
        sysClassVo.setStudents(stus);
        //查询教师数
        Wrapper<SysTeacherClass> sysTeacherClassWrapper = new EntityWrapper<>();
        sysTeacherClassWrapper.eq("CLASS_ID", id);
        sysTeacherClassWrapper.and().eq("IS_VALID", 1);
//        sysTeacherClassWrapper
        List<SysTeacherClass> sysTeacherClasses = sysTeacherClassMapper.selectList(sysTeacherClassWrapper);
        //根据教师id进行去重
        if (!CollectionUtils.isEmpty(sysTeacherClasses)) {
            sysTeacherClasses = sysTeacherClasses.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(SysTeacherClass::getUserId))), ArrayList::new));
            sysClassVo.setTeachers(sysTeacherClasses.size());
        }
        return sysClassVo;
    }
}