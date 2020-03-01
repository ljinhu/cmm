package com.yi.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yi.entity.SysUser;

/**
 *
 * SysUser 表数据库控制层接口
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

	List<Map<Object, Object>> selectUserList(Page<Map<Object, Object>> page, @Param("search") String search);

	/**
	 * 根据角色名称获取用户
	 * @param roleName
	 * @return
	 */
	List<SysUser> selectUserByRole(String roleName);
}