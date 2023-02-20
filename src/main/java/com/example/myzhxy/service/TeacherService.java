package com.example.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myzhxy.pojo.LoginForm;
import com.example.myzhxy.pojo.Teacher;

public interface TeacherService extends IService<Teacher> {
    Teacher login(LoginForm loginForm);

    Teacher getByTeacherById(Long userId);

    IPage<Teacher> getTeacherByOpr(Page<Teacher> pageParam, Teacher teacher);
}
