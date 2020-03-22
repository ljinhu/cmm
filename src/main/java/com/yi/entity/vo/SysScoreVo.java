package com.yi.entity.vo;

import com.yi.entity.SysScore;

import java.util.List;

public class SysScoreVo extends SysScore {

    List<String>  lessonCodes;

    public List<String> getLessonCodes() {
        return lessonCodes;
    }

    public void setLessonCodes(List<String> lessonCodes) {
        this.lessonCodes = lessonCodes;
    }
}
