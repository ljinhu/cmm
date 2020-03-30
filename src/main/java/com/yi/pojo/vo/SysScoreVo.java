package com.yi.pojo.vo;

import com.yi.pojo.SysScore;

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
