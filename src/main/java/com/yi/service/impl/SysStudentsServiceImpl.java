package com.yi.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yi.common.bean.Rest;
import com.yi.common.util.ImportExcelUtil;
import com.yi.common.util.ShiroUtil;
import com.yi.entity.SysClass;
import com.yi.entity.SysStudents;
import com.yi.entity.SysUser;
import com.yi.entity.SysUserRole;
import com.yi.entity.vo.StudentVo;
import com.yi.mapper.SysStudentsMapper;
import com.yi.mapper.SysUserMapper;
import com.yi.mapper.SysUserRoleMapper;
import com.yi.service.ISysStudentsService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/2/28 15:16
 * @Description:
 */
@Service
public class SysStudentsServiceImpl extends ServiceImpl<SysStudentsMapper, SysStudents> implements ISysStudentsService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    private final String parentRoleId = "a3e54005aa7243078b11791a600f40b9";

    @Override
    public Page<SysStudents> findByPage(SysStudents sysStudents, Page<SysStudents> page) {
        //构建查询条件
        Wrapper<SysStudents> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(sysStudents.getName())) {
            wrapper.like("name", sysStudents.getName());
        }

        if (StringUtils.isNotEmpty(sysStudents.getClassId())) {
            wrapper.and().eq("CLASS_ID", sysStudents.getClassId());
        }

        //执行查询
        Page<SysStudents> sysStudentsPage = this.selectPage(page, wrapper);
        return sysStudentsPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Rest save(StudentVo studentVo) {
        try {
            SysStudents sysStudents = new SysStudents();
            SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
            sysStudents.setCreatedBy(principal.getUserName());
            sysStudents.setCreatedTime(new Date());
            BeanUtils.copyProperties(studentVo, sysStudents);
            //如果sysStudents含有id，时修改，不再保存user即家长信息了
            if (!StringUtils.isNotEmpty(studentVo.getId())) {
                //构建家长信息
                SysUser sysUser = new SysUser();
                sysUser.setUserName(studentVo.getParentName());
                sysUser.setCreateTime(new Date());
                sysUser.setUserState(1);
                sysUser.setPassword(ShiroUtil.md51024Pwd(studentVo.getPassword(), studentVo.getParentName()));
                //保存用户
                userMapper.insert(sysUser);

                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(parentRoleId);
                userRoleMapper.insert(sysUserRole);
                sysStudents.setParentId(sysUser.getId());
                sysStudents.setParentName(sysUser.getUserName());
            }
            this.insertOrUpdate(sysStudents);
        }catch (Exception e){
            e.printStackTrace();
            return Rest.failure("发生异常");
        }
        return Rest.ok("保存成功");
    }

    @Override
    public Rest checkNo(String no) {
        if (StringUtils.isEmpty(no)) {
            return Rest.ok();
        }
        Wrapper<SysStudents> wrapper = new EntityWrapper<>();
        wrapper.eq("no", no);
        int i = this.selectCount(wrapper);
        //查到多于0条数据，说明已经有存在学号为no的，重复
        if (i > 0) {
            return Rest.failure("学号重复");
        }
        return Rest.ok();

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Rest importExcel(MultipartFile multipartFile) throws Exception{
        InputStream in = multipartFile.getInputStream();
        List<List<Object>> list = ImportExcelUtil.getBankListByExcel(in, multipartFile.getOriginalFilename());
        in.close();
        if(!CollectionUtils.isEmpty(list)){
            for (List<Object> objectList : list){
                StudentVo studentVo = new StudentVo();
                if(!CollectionUtils.isEmpty(objectList)){
                    for (int i = 0;i < objectList.size();i++){
                        switch (i){
                            case 0:
                                studentVo.setName(String.valueOf(objectList.get(i)));
                                break;
                            case 1:
                                studentVo.setNo(String.valueOf(objectList.get(i)));break;
                            case 2:
                                studentVo.setClassId(String.valueOf(objectList.get(i)));break;
                            case 3:
                                studentVo.setParentName(String.valueOf(objectList.get(i))); break;
                            case 4:
                                studentVo.setPassword(String.valueOf(objectList.get(i))); break;
                        }
                    }
                }

                if(!StringUtils.isEmpty(studentVo.getName()) && StringUtils.isNotEmpty(studentVo.getPassword())){
                    this.save(studentVo);
                }
            }
        }
        return Rest.ok();
    }
}