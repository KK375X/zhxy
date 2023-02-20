package com.example.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myzhxy.mapper.StudentMapper;
import com.example.myzhxy.pojo.Admin;
import com.example.myzhxy.pojo.LoginForm;
import com.example.myzhxy.pojo.Student;
import com.example.myzhxy.service.StudentService;
import com.example.myzhxy.util.MD5;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("studentServiceImpl")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @ApiOperation("登录操作")
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        /**
         * queryWrapper.eq() ：判断数据库中的第一个参数与第二个参数是否相同
         */
        queryWrapper.eq("name", loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword())); // MD5加密
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }


    @ApiOperation("页面跳转操作")
    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }


    @ApiOperation("分页模糊查询学生信息")
    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        // 调用SQL语句的类
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        // 获取年级关键字并判断
        if (!StringUtils.isEmpty(student.getName())) {
            queryWrapper.like("name", student.getName());
        }
        if (!StringUtils.isEmpty(student.getClazzName())) {
            queryWrapper.like("clazz_name", student.getClazzName());
        }

        // 升序排序
        queryWrapper.orderByAsc("id");

        // 查询数据库并封装回来
        Page<Student> studentPage = baseMapper.selectPage(pageParam, queryWrapper);

        return studentPage;
    }
}
