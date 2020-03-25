package com.yi.controller.system;

import com.yi.common.controller.BaseController;
import com.yi.entity.SysComment;
import com.yi.service.SysCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:
 * @Date: 2020/3/24 16:44
 * @Description:
 */
@Controller
@RequestMapping("/system/comment")
public class SysCommentController extends BaseController {

    private final String prefix = "/system/comment/";

    @Autowired
    private SysCommentService commentService;

    /**
     * 教师通过班级列表进入查看所教课程班级所有学生表现
     * 班主任通过班级列表进入查看所有表现
     * 家长直接查看所有表现
     * @param model
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(Model model, @PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,String stuId) {

        return prefix + "list";
    }
}