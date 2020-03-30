package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.common.bean.Rest;
import com.yi.pojo.SysClass;
import com.yi.pojo.SysStudents;
import com.yi.pojo.vo.StudentVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/28 15:14
 * @Description: 
 */
public interface ISysStudentsService extends IService<SysStudents> {

    /**
     * 按条件分页查询
     * @param sysStudents
     * @param page
     * @return
     */
    Page<SysStudents> findByPage(SysStudents sysStudents, Page<SysStudents> page, List<SysClass> classes);

    Rest save(StudentVo studentVo);

    /**
     * 检查学号是否重复
     * @param no
     * @return
     */
    Rest checkNo(String no);

    Rest importExcel(MultipartFile multipartFile) throws Exception;

    Rest delete(String id);

    /**
     * 根据家长用户id查询学生
     * @param pid
     * @return
     */
    SysStudents getByPid(String pid);

    SysStudents getByNo(String no);
}