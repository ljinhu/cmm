package com.yi.controller.system;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.entity.SysClass;
import com.yi.entity.SysNotice;
import com.yi.entity.SysUser;
import com.yi.service.ISysClassService;
import com.yi.service.SysNoticeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    private final String prefix = "system/notice/";

    @Autowired
    private SysNoticeService noticeService;
    @Autowired
    private ISysClassService classService;

    /**
     * 班主任分页查询公告
     *
     * @param pageNo      第几页
     * @param pageSize    每页显示的数据
     * @param name        班级名称
     * @param model
     * @return
     */
    @RequestMapping("/list/{pageNo}")
    public String list(@PathVariable Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize,
                       String name,String classNo, Model model) {
        Page<SysNotice> page = getPage(pageNo, pageSize);
        SysNotice notice = new SysNotice();
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        notice.setCreatedBy(user.getId());
        if(StringUtils.isNotEmpty(name)) {
            notice.setName(name);
            model.addAttribute("name", name);
        }
        if(StringUtils.isNotEmpty(classNo)){
            notice.setClassNo(classNo);
            model.addAttribute("classNo",classNo);
        }
        Page<SysNotice> noticePage = noticeService.findPage(page, notice);
        model.addAttribute("pageData",noticePage);
        return prefix + "list";
    }

    @RequestMapping("/toAdd")
    public String toAdd(Model model){
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        Wrapper<SysClass> wrapper = new EntityWrapper<>();
        wrapper.eq("charge_uid",sysUser.getId());
        wrapper.eq("is_valid",1);
        List<SysClass> classes = classService.selectList(wrapper);
        model.addAttribute("classes",classes);
        return prefix + "add";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Rest doAdd(SysNotice notice){
        if(this.noticeService.save(notice)){
            return Rest.ok("发布成功");
        }else{
            return Rest.failure("发布失败");
        }
    }

    @RequestMapping("/delete/{id}")
    @RequiresPermissions("system:notice:del")
    @ResponseBody
    public Rest delete(@PathVariable String id){
        try {
            noticeService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return Rest.failure("删除失败");
        }
        return Rest.ok("删除成功");
    }

    @RequestMapping("/detail/{id}")
    public String detail(Model model,@PathVariable String id){
        SysNotice notice = noticeService.selectById(id);
        model.addAttribute("notice",notice);
        return prefix + "detail";
    }
}
