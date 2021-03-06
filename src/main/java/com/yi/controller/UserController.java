package com.yi.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.yi.common.anno.Log;
import com.yi.common.bean.Rest;
import com.yi.common.controller.BaseController;
import com.yi.pojo.SysRole;
import com.yi.pojo.SysUser;
import com.yi.pojo.SysUserRole;
import com.yi.service.ISysDeptService;
import com.yi.service.ISysRoleService;
import com.yi.service.ISysUserRoleService;
import com.yi.service.ISysUserService;
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
import java.util.Map;
import java.util.Optional;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

	@Autowired private ISysUserService sysUserService;
	@Autowired private ISysRoleService sysRoleService;
	@Autowired private ISysUserRoleService sysUserRoleService;
	@Autowired private ISysDeptService sysDeptService;
	
	/**
	 * 分页查询用户
	 */
	@RequiresPermissions("listUser")
    @RequestMapping("/list/{pageNumber}")  
    public  String list(@PathVariable Integer pageNumber,@RequestParam(defaultValue="15") Integer pageSize,String search,Model model){
		if(StringUtils.isNotBlank(search)){
			model.addAttribute("search", search);
		}
    	Page<Map<Object, Object>> page = getPage(pageNumber,pageSize);
    	model.addAttribute("pageSize", pageSize);
    	Page<Map<Object, Object>> pageData = sysUserService.selectUserPage(page, search);
    	model.addAttribute("pageData", pageData);
    	return "system/user/list";
    } 
    /**
     * 新增用户
     */
	@RequiresPermissions("addUser")
    @RequestMapping("/add")  
    public  String add(Model model){
		Wrapper<SysRole> roleWrapper = new EntityWrapper<>();
		roleWrapper.ne("id",parent_id);
    	model.addAttribute("roleList", sysRoleService.selectList(roleWrapper));
//    	model.addAttribute("deptList", sysDeptService.selectList(null));
		return "system/user/add";
    } 
    
    /**
     * 执行新增
     */
    @Log("创建用户")
    @RequiresPermissions("addUser")
    @RequestMapping("/doAdd")  
    @ResponseBody
    public  Rest doAdd(SysUser user,@RequestParam(value="roleId",required=false) String roleId){
    	String[] roleIds = {roleId};
    	sysUserService.insertUser(user,roleIds);
    	return Rest.ok();
    }  
    /**
     * 删除用户
     */
    @Log("删除用户")
    @RequiresPermissions("deleteUser")
    @RequestMapping("/delete")  
    @ResponseBody
    public  Rest delete(String id){
    	sysUserService.delete(id);
    	return Rest.ok();
    }  
    
	/**
	 * 编辑用户
	 */
    @RequestMapping("/edit/{id}")  
    @RequiresPermissions("editUser")
    public  String edit(@PathVariable String id,Model model){
    	SysUser sysUser = sysUserService.selectById(id);
		Wrapper<SysRole> roleWrapper = new EntityWrapper<>();
		roleWrapper.ne("id",parent_id);
    	List<SysRole> sysRoles = sysRoleService.selectList(roleWrapper);
    	EntityWrapper<SysUserRole> ew = new EntityWrapper<SysUserRole>();
    	ew.eq("userId ", id);
    	List<SysUserRole> mySysUserRoles = sysUserRoleService.selectList(ew);
    	List<String> myRolds = Lists.transform(mySysUserRoles, input -> input.getRoleId());
		List<String> strings = Optional.ofNullable(myRolds).orElse(new ArrayList<>());
		long count = strings.stream().filter(s -> s.equals(parent_id)).count();
		if(count > 0){
			model.addAttribute("isParent",true);
		}
		model.addAttribute("sysUser",sysUser);
    	model.addAttribute("sysRoles",sysRoles);
    	model.addAttribute("myRolds",myRolds);
    	model.addAttribute("deptList", sysDeptService.selectList(null));
    	return "system/user/edit";
    } 
    /**
     * 执行编辑
     */
    @Log("编辑用户")
    @RequiresPermissions("editUser")
    @RequestMapping("/doEdit")  
    @ResponseBody
    public  Rest doEdit(SysUser sysUser,@RequestParam(value="roleId",required=false) String roleId,Model model){
    	String[] roleIds = {roleId};
    	sysUserService.updateUser(sysUser,roleIds);
    	return Rest.ok();
    } 
    
    /**
     * 验证用户名是否已存在
     */
    @RequestMapping("/checkName")  
    @ResponseBody
    public Rest checkName(String userName,String parentName){
		Wrapper<SysUser> wrapper = new EntityWrapper<SysUser>().eq("userName", userName).or().eq("userName", parentName);
		List<SysUser> list = sysUserService.selectList(wrapper);
    	if(list.size() > 0){
    		return Rest.failure("用户名已存在");
    	}
    	return Rest.ok();
    }
    
}
