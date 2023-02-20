package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myzhxy.pojo.Teacher;
import com.example.myzhxy.service.TeacherService;
import com.example.myzhxy.util.MD5;
import com.example.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 不是@Controller
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("获取全部教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码号") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页面大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("搜索的关键字集合") Teacher teacher
    ) {
        Page<Teacher> pageParam = new Page<>(pageNo, pageSize);
        IPage<Teacher> iPage = teacherService.getTeacherByOpr(pageParam, teacher);
        return Result.ok(iPage);
    }


    @ApiOperation("添加或者修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("以JSON格式传递数据") @RequestBody Teacher teacher
    ) {
        // 如果是新增的用户，要对密码进行加密
        Integer id = teacher.getId();
        if (null == id || 0 == id) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    @ApiOperation("删除或批量删除")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @ApiParam("以JSON格式传递数据")
            @RequestBody List<Integer> ids
    ) {
        teacherService.removeByIds(ids);
        return Result.ok();
    }

}
