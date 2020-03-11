package com.yi.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.Dict;
import com.yi.entity.DictType;
import com.yi.entity.SysClass;
import com.yi.entity.SysUser;
import com.yi.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private DictService dictService;
    @Autowired
    private DictTypeService dictTypeService;
    /**
     * 分页查询班级
     *
     * @param pageNo      第几页
     * @param pageSize    每页显示的数据
     * @param name        班级名称
     * @param model
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name, Model model) {
        Page<SysClass> page = getPage(pageNo, pageSize);
        SysClass sysClass = new SysClass();
        sysClass.setName(name);
        Page<SysClass> sysClassPage = sysClassService.findPage(sysClass, page);
        model.addAttribute("pageData", sysClassPage);
        model.addAttribute("name", name);
        return PREFIX + "list";
    }

    /**
     * 跳转到添加添加或修改页面
     * @return
     */
    @RequiresPermissions({"system:class:add"})
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
        List<SysUser> users = sysRoleService.getSysUsers("a1d79fef008c4e40bb3d4398e170ada1");
        model.addAttribute("charges",users);
        //获取年级
        List<Dict> grades = dictService.findByTypeCode("grade");
        model.addAttribute("grades",grades);
        return PREFIX +"add";
    }

    /**
     * 执行添加或修改
     * @return
     */
    @RequiresPermissions({"system:class:add"})
    @RequestMapping("/add")
    @ResponseBody
    public Rest doAdd(SysClass sysClass){
        return sysClassService.save(sysClass);
    }

    @RequestMapping("/delete")
    @RequiresPermissions("system:class:del")
    @ResponseBody
    public Rest delete(String id, Long isValid){
        this.sysClassService.del(id,isValid);
        return Rest.ok("操作成功");
    }

    /**
     * 我的班级页展示班级图片、教师信息。
     * @return
     */
    public String mineClass(){
        return "";
    }
}