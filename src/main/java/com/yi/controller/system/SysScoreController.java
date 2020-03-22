package com.yi.controller.system;


import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysExams;
import com.yi.entity.SysScore;
import com.yi.entity.vo.SysScoreVo;
import com.yi.service.SysScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/score")
public class SysScoreController extends BaseController {
    @Autowired
    private SysScoreService sysScoreService;
    private final String prefix = "/system/score/";

    /**
     * 学生成绩录入
     *
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model, String examId) {

        return prefix + "add";
    }

    @RequestMapping("/list/{pageNo}")
    public String list(Model model, SysScoreVo score, @PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<SysScore> page = getPage(pageNo, pageSize);
        String role = "";
        if (isCharge()) {
            role = "manager";
        } else if (isTeacher()) {
            role = "teacher";
        } else {
            role = "parent";
        }
        Page<SysScore> scorePage = sysScoreService.getScore(score, page, role);
        model.addAttribute("pageData",scorePage);
        return prefix + "list";
    }
}
