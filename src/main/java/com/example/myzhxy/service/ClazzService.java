package com.example.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myzhxy.pojo.Clazz;

import java.util.List;

public interface ClazzService extends IService<Clazz> {
    IPage<Clazz> getClazzsByOpr(Page<Clazz> page, Clazz clazz);

    List<Clazz> getClazzs();
}
