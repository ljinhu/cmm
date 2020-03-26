package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.Dict;
import com.yi.entity.SysClass;
import com.yi.entity.SysHomeWork;
import com.yi.entity.SysTeacherClass;
import com.yi.mapper.SysHomeWorkMapper;
import com.yi.service.DictService;
import com.yi.service.ISysClassService;
import com.yi.service.SysHomeWorkService;
import com.yi.service.SysTeacherClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class SysHomeWorkServiceImpl extends ServiceImpl<SysHomeWorkMapper, SysHomeWork> implements SysHomeWorkService {
    @Autowired
    private ISysClassService classService;
    @Autowired
    private DictService dictService;
    @Autowired
    private SysTeacherClassService teacherClassService;

    @Override
    public Page<SysHomeWork> getPageList(SysHomeWork work, Page<SysHomeWork> page, List<String> classIds) {
        Wrapper<SysHomeWork> workWrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(work.getClassId())) {
            //只查询一个class的数据
            workWrapper.eq("CLASS_ID", work.getClassId());
        } else {
            if (!CollectionUtils.isEmpty(classIds)) {
                workWrapper.in("CLASS_ID", classIds);
            } else {
                workWrapper.eq("CLASS_ID", "-1");
            }
        }
        if(StringUtils.isNotEmpty(work.getCreatedBy())){
            workWrapper.and().eq("CREATED_BY",work.getCreatedBy());
        }
        return this.selectPage(page, workWrapper);
    }

    @Override
    public boolean save(SysHomeWork work) throws Exception {
        String classId = work.getClassId();
        if (classId == null) {
            return false;
        }
        //先查教师-班级关联表
        SysTeacherClass sysTeacherClass = teacherClassService.selectById(classId);
        if(null == sysTeacherClass){
            throw  new Exception("无法查到相关信息");
        }
        classId = sysTeacherClass.getClassId();
        work.setClassId(classId);
        //获取班级信息
        SysClass sysClass = classService.selectById(classId);
        if (null == sysClass) {
            throw new Exception("保存作业时，班级不存在");
        }
        work.setClassName(sysClass.getName());
        String lessonCode = sysTeacherClass.getLessonCode();
        work.setLessonCode(lessonCode);
        if (StringUtils.isEmpty(lessonCode)) {
            throw new Exception("保存作业时，无法查询到所对应的课程");
        }
        List<Dict> lessons = dictService.selectList("lesson", lessonCode);
        if (CollectionUtils.isEmpty(lessons)) {
            throw new Exception("保存作业时，无法查询到所对应的课程");
        }
        Dict dict = lessons.get(0);
        work.setLessonName(dict.getName());
        work.setCreatedTime(new Date());
        this.insertOrUpdate(work);
        return true;
    }
}
