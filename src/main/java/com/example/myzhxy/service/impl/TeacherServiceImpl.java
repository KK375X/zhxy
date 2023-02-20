package com.example.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myzhxy.mapper.TeacherMapper;
import com.example.myzhxy.pojo.Admin;
import com.example.myzhxy.pojo.LoginForm;
import com.example.myzhxy.pojo.Teacher;
import com.example.myzhxy.service.TeacherService;
import com.example.myzhxy.util.MD5;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("teacherServiceImpl")
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @ApiOperation("登录操作")
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        /**
         * queryWrapper.eq() ：判断数据库中的第一个参数与第二个参数是否相同
         */
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword())); // MD5加密
        Teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @ApiOperation("页面跳转操作")
    @Override
    public Teacher getByTeacherById(Long userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }


    @ApiOperation("获取全部教师信息")
    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getName())) {
            queryWrapper.like("name", teacher.getName());
        }
        if (!StringUtils.isEmpty(teacher.getClazzName())) {
            queryWrapper.like("clazz_name", teacher.getClazzName());
        }
        queryWrapper.orderByAsc("id");
        Page<Teacher> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }


}
