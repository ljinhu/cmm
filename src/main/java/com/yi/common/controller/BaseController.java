package com.yi.common.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yi.pojo.SysUser;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.common.util.HttpUtil;

/**
 * 基础控制器
 * @date 2019年12月27日 上午11:50:57
 */
public class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 系统中学生用户id
	 */
	private final String stu_id = "6b008d1d88da4c488f05c9a634651cae";

	protected final String teach_id = "6d0a4b3f820b4b39aa158ff7cfc3001a";

	protected final String charge_id = "a1d79fef008c4e40bb3d4398e170ada1";

	protected final String parent_id = "a3e54005aa7243078b11791a600f40b9";

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected HttpSession session;

	@Autowired
	protected ServletContext application;

	/**
	 * 是否为 post 请求
	 */
	protected boolean isPost() {
		return HttpUtil.isPost(request);
	}


	/**
	 * 是否为 get 请求
	 */
	protected boolean isGet() {
		return HttpUtil.isGet(request);
	}


	/**
	 * <p>
	 * 获取分页对象
	 * </p>
	 */
	protected <T> Page<T> getPage(int pageNumber) {
		return getPage(pageNumber,15);
	}


	/**
	 * <p>
	 * 获取分页对象
	 * </p>
	 * 
	 * @param size
	 *            每页显示数量
	 * @return
	 */
	protected <T> Page<T> getPage( int pageNumber,int pageSize) {
		return new Page<T>(pageNumber, pageSize);
	}


	/**
	 * 重定向至地址 url
	 * 
	 * @param url
	 *            请求地址
	 * @return
	 */
	protected String redirectTo( String url ) {
		StringBuffer rto = new StringBuffer("redirect:");
		rto.append(url);
		return rto.toString();
	}


	/**
	 * 
	 * 返回 JSON 格式对象
	 * 
	 * @param object
	 *            转换对象
	 * @return
	 */
	protected String toJson( Object object ) {
		return JSON.toJSONString(object, SerializerFeature.BrowserCompatible);
	}


	/**
	 * 
	 * 返回 JSON 格式对象
	 * 
	 * @param object
	 *            转换对象
	 * @param
	 * @return
	 */
	protected String toJson( Object object, String format ) {
		if ( format == null ) {
			return toJson(object);
		}
		return JSON.toJSONStringWithDateFormat(object, format, SerializerFeature.WriteDateUseDateFormat);
	}

	protected boolean isParent(){
		return SecurityUtils.getSubject().hasRole(parent_id);
	}

	protected boolean isTeacher(){
		return SecurityUtils.getSubject().hasRole(teach_id);
	}
	protected boolean isCharge(){
		return SecurityUtils.getSubject().hasRole(charge_id);
	}

	protected SysUser cuurenUser(){
		SysUser sysUser = (com.yi.pojo.SysUser) SecurityUtils.getSubject().getPrincipal();
		return sysUser;
	}

}
