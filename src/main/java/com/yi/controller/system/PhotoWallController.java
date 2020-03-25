package com.yi.controller.system;

import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.PhotoWall;
import com.yi.entity.SysClass;
import com.yi.entity.vo.PhotoWallPoJo;
import com.yi.service.ISysClassService;
import com.yi.service.PhotoWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/3/25 16:24
 * @Description: 
 */
@Controller
@RequestMapping("/system/photo")
public class PhotoWallController extends BaseController {

    private final String prefix = "/system/photo";
    @Autowired
    private PhotoWallService photoWallService;
    @Autowired
    private ISysClassService classService;

    public String toAdd(String classId, Model model){
        List<PhotoWall> photoWalls = photoWallService.findByClassId(classId);
        SysClass sysClass = classService.selectById(classId);
        model.addAttribute("photoWalls",photoWalls);
        model.addAttribute("sysClass",sysClass);
        return prefix + "add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Rest doAdd(PhotoWallPoJo photoWallPoJo){
        return Rest.ok();
    }

}