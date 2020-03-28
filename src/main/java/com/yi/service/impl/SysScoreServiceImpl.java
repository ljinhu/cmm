package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.entity.*;
import com.yi.entity.vo.SysScoreVo;
import com.yi.mapper.SysScoreMapper;
import com.yi.service.ISysStudentsService;
import com.yi.service.SysExamsService;
import com.yi.service.SysScoreService;
import com.yi.service.SysTeacherClassService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysScoreServiceImpl extends ServiceImpl<SysScoreMapper, SysScore> implements SysScoreService {
    @Autowired
    private ISysStudentsService sysStudentsService;
    @Autowired
    private SysScoreMapper sysScoreMapper;
    @Autowired
    private SysTeacherClassService teacherClassService;
    @Autowired
    private SysExamsService sysExamsService;

    @Override
    public Page<SysScore> getScore(SysScoreVo score, Page<SysScore> page, String roleName) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        Wrapper<SysStudents> studentsWrapper = new EntityWrapper<>();
        if ("manager".equals(roleName)) {
            //是班主任直接根据class_id查询所有
            Wrapper<SysScore> scoreWrapper = new EntityWrapper<>();
            scoreWrapper.eq("class_id", score.getClassId());
            if(!StringUtils.isEmpty(score.getExamId())){
                scoreWrapper.eq("exam_id", score.getExamId());
            }
            return this.selectPage(page, scoreWrapper);

        } else if ("teacher".equals(roleName)) {
            //是教师需要知道这个老师是哪门课程
            Wrapper<SysTeacherClass> stwrp = new EntityWrapper<>();
            stwrp.eq("CLASS_ID", score.getClassId());
            stwrp.and().eq("user_Id", user.getId());
            List<SysTeacherClass> ts = teacherClassService.selectList(stwrp);
            if (!CollectionUtils.isEmpty(ts)) {
                List<String> collect = ts.stream().map(SysTeacherClass::getLessonCode).collect(Collectors.toList());
                Wrapper<SysScore> scoreWrapper = new EntityWrapper<>();
                scoreWrapper.eq("class_id", score.getClassId());
                scoreWrapper.in("lesson_Code", collect);
                if(!StringUtils.isEmpty(score.getExamId())){
                    scoreWrapper.eq("exam_id", score.getExamId());
                }
               return this.selectPage(page, scoreWrapper);
            }

        } else {
            //是家长，先查学生,直接通过stu_no查询
            SysStudents students = sysStudentsService.getByPid(user.getId());
            if (null != students) {
                score.setStuNo(students.getNo());
            }
            Wrapper<SysScore> scoreWrapper = new EntityWrapper<>();
            scoreWrapper.eq("class_id", score.getClassId());
            scoreWrapper.eq("stu_no", score.getStuNo());
            if(!StringUtils.isEmpty(score.getExamId())){
                scoreWrapper.eq("exam_id", score.getExamId());
            }
            return this.selectPage(page, scoreWrapper);
        }
        return page;
    }

    @Override
    public boolean save(SysScore sysScore, SysUser sysUser) {
        try {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(sysScore.getId())) {
                SysScore scoreDb = this.selectById(sysScore.getId());
                scoreDb.setScore(sysScore.getScore());
                scoreDb.setComment(sysScore.getComment());
                scoreDb.setCreatedBy(sysUser.getUserName());
                scoreDb.setCreatedTime(new Date());
                scoreDb.setCreatedId(sysUser.getId());
                this.updateById(scoreDb);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<SysScore> getAllScoreByExamCode(String code,String stuNo) {
        if(!StringUtils.isEmpty(code)){
            //先根据code查询考试id
            Wrapper<SysExams> exmWrp = new EntityWrapper<>();
            exmWrp.eq("code", code);
            List<SysExams> sysExams = sysExamsService.selectList(exmWrp);
            if(!CollectionUtils.isEmpty(sysExams)){
                List<String> examIds = sysExams.stream().map(SysExams::getId).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(examIds)){
                    //根据考试的id去查成绩
                    Wrapper<SysScore> wrapper = new EntityWrapper<>();
                    wrapper.in("exam_id",examIds);
                    if(!StringUtils.isEmpty(stuNo)){
                        wrapper.eq("stu_no", stuNo);
                    }
                    List<SysScore> sysScores = this.selectList(wrapper);
                    return sysScores;
                }
            }
        }
        return null;
    }

}
