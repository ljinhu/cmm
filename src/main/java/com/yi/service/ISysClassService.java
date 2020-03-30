package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.common.bean.Rest;
import com.yi.pojo.SysClass;
import com.yi.pojo.SysUser;
import com.yi.pojo.vo.SysClassVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/24 15:20
 * @Description: 
 */
public interface ISysClassService extends IService<SysClass> {

    Page<SysClass> findPage(SysClass sysClass,Page<SysClass> page);

    Rest save(SysClass sysClass);

    /**
     * 根据角色名称获取用户
     * @param roleName
     * @return
     */
    List<SysUser> selectUserByRole(String roleName);

    void del(String id,Long isValid);

    /**
     * 通过excel导入学生
     * @param file
     * @return
     */
    Rest importStuExcel(MultipartFile file);

    /**
     * 将学生移出班级
     * @param stuId
     * @param classId
     * @return
     */
    boolean removeStudent(String stuId,String classId) throws Exception;

    SysClass getByClassNo(String classNo);

    /**
     * 获取家长对应的班级
     * @param pid
     * @param isValid 是否失效，null的话查询所有状态的
     * @return
     */
    List<SysClass> getClassesByPid(String pid,Long isValid);

    List<SysClass> getClassByChargeUid(String pid,Long isValid);

    /**
     * 班级详情，包含照片等信息
     * @param id
     * @return
     */
    SysClassVo detail(String id);
}