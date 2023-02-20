package com.example.myzhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myzhxy.mapper.ClazzMapper;
import com.example.myzhxy.pojo.Clazz;
import com.example.myzhxy.service.ClazzService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {

    @ApiOperation("分页模糊查询班级信息")
    @Override
    public IPage<Clazz> getClazzsByOpr(Page<Clazz> pageParam, Clazz clazz) {
        // 调用SQL语句的类
        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();

        // 获取年级关键字并判断
        String gradeName = clazz.getGradeName();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("grade_name", gradeName);
        }

        // 获取班级关键字并判断
        String name = clazz.getName();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }

        // 升序排序
        queryWrapper.orderByAsc("id");

        // 查询数据库并封装回来
        Page<Clazz> clazzPage = baseMapper.selectPage(pageParam, queryWrapper);

        // 返回到前端
        return clazzPage;
    }


    @ApiOperation("查询所以班级信息（下拉框里的内容）")
    @Override
    public List<Clazz> getClazzs() {
        return baseMapper.selectList(null);
    }
}
