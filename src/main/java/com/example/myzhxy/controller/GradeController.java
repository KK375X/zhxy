package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myzhxy.pojo.Grade;
import com.example.myzhxy.service.GradeService;
import com.example.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 下面三个注入都起到解释说明的作用
 * @Api
 * @ApiOperation
 * @ApiParam
 */

@Api(tags = "年级控制器")
@RestController // 不是@Controller
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("根据年级名称进行模糊查询")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数")
            @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页面大小")
            @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的模糊关键字")
            String gradeName
    ) {
        // 分页带条件查询
        Page<Grade> page = new Page<Grade>(pageNo, pageSize);

        // 通过服务层完成查询
        IPage<Grade> pageRs = gradeService.getGradeByOpy(page, gradeName);
        // 封装Result对象并返回
        return Result.ok(pageRs);
    }


    // 由于saveOrUpdate()方法是springboot给定的方法，因此不需要单独实现，直接调用即可。
    @ApiOperation("添加和更新班级信息")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON的grade的对象")
            @RequestBody Grade grade
    ) {
        // 接受参数
        // 调用服务层方法完成增减或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


    @ApiOperation("删除和批量删除")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除Grade的id的JSON集合")
            @RequestBody List<Integer> ids
    ) {
        // 将选中的ids封装到数据库
        gradeService.removeByIds(ids);

        // 返回结果
        return Result.ok();
    }


    @ApiOperation("获取全部年级信息（下拉框里的内容）")
    @GetMapping("/getGrades")
    public Result getGrades() {
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

}
