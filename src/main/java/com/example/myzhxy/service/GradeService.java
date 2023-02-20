package com.example.myzhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.myzhxy.pojo.Grade;

import java.util.List;

public interface GradeService extends IService<Grade> {
    IPage<Grade> getGradeByOpy(Page<Grade> page, String gradeName);

    List<Grade> getGrades();
}
