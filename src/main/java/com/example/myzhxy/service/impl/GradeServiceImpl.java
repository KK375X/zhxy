package com.example.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myzhxy.mapper.GradeMapper;
import com.example.myzhxy.pojo.Grade;
import com.example.myzhxy.service.GradeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {

    @ApiOperation("条件查询")
    @Override
    public IPage<Grade> getGradeByOpy(Page<Grade> pageParam, String gradeName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)) {
            // 模糊查询
            queryWrapper.like("name", gradeName);
        }

        // 按照 id 升序排序
        queryWrapper.orderByAsc("id");

        /**
         * 查询
         * pageParam ：查询对象
         * queryWrapper ：查询条件
         */
        Page<Grade> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;
    }


    @ApiOperation("获取全部年级信息（下拉框里的内容）")
    @Override
    public List<Grade> getGrades() {
        return baseMapper.selectList(null);
    }
}
