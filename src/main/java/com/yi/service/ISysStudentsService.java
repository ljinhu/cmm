package com.yi.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.yi.common.bean.Rest;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.vo.StudentVo;
import org.springframework.web.multipart.MultipartFile;

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
    Page<SysStudents> findByPage(SysStudents sysStudents,Page<SysStudents> page);

    Rest save(StudentVo studentVo);

    /**
     * 检查学号是否重复
     * @param no
     * @return
     */
    Rest checkNo(String no);

    Rest importExcel(MultipartFile multipartFile) throws Exception;
}