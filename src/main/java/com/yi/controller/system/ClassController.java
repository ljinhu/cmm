package com.yi.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysClass;
import com.yi.service.ISysClassService;
import com.yi.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author:
 * @Date: 2020/2/24 15:19
 * @Description:
 */
@Controller
@RequestMapping("/system/class")
public class ClassController extends BaseController {
    private final String PREFIX = "system/class/";
    @Autowired
    private ISysClassService sysClassService;
    @Autowired
    private ISysUserService  userService;

    /**
     * 分页查询班级
     *
     * @param pageNo      第几页
     * @param pageSize    每页显示的数据
     * @param name        班级名称
     * @param chargeUname 班主任名称
     * @param model
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, String chargeUname, Model model) {
        Page<SysClass> page = getPage(pageNo, pageSize);
        SysClass sysClass = new SysClass();
        sysClass.setChargeUname(chargeUname);
        sysClass.setName(name);
        Page<SysClass> sysClassPage = sysClassService.findPage(sysClass, page);
        model.addAttribute("page", sysClassPage);
        model.addAttribute("name", name);
        model.addAttribute("chargeUname", chargeUname);
        return PREFIX + "list";
    }

    /**
     * 跳转到添加添加或修改页面
     * @return
     */
    @RequestMapping("/toAdd")
    public String toAdd(Model model,String id){
        SysClass sysClass = new SysClass();
        if(StringUtils.isNotEmpty(id)){
            //如果id不为空，去查询
            sysClass = sysClassService.selectById(id);
            //如果查到，说明时修改，需要把原数据传到表单上回显
            model.addAttribute("sysClass",sysClass);
        }
        //获取班主任列表，供添加时选择班主任

        return PREFIX +"add";
    }

    /**
     * 执行添加或修改
     * @return
     */
    @RequestMapping("/add")
    public Rest doAdd(SysClass sysClass){
        return sysClassService.save(sysClass);
    }

}