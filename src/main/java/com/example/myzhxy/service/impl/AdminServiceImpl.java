package com.example.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myzhxy.mapper.AdminMapper;
import com.example.myzhxy.pojo.Admin;
import com.example.myzhxy.pojo.LoginForm;
import com.example.myzhxy.service.AdminService;
import com.example.myzhxy.util.MD5;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("adminServiceImpl")
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @ApiOperation("登录操作")
    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        /**
         * queryWrapper.eq() ：判断数据库中的第一个参数与第二个参数是否相同
         */
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @ApiOperation("页面跳转操作")
    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return baseMapper.selectOne(queryWrapper);
    }


    @ApiOperation("分页查询管理员信息")
    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> pageParam, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("name", adminName);
        }
        queryWrapper.orderByAsc("id");
        Page<Admin> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }
}
