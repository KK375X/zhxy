package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myzhxy.pojo.Student;
import com.example.myzhxy.service.StudentService;
import com.example.myzhxy.util.MD5;
import com.example.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 不是@Controller
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    StudentService studentService;

    @ApiOperation("分页模糊查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页面大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询的关键字") Student student
    ) {
        // 分页信息封装page对象
        Page<Student> pageParam = new Page<>(pageNo, pageSize);

        // 进行分页查询
        IPage<Student> studentPage = studentService.getStudentByOpr(pageParam, student);

        // 返回到前端
        return Result.ok(studentPage);
    }


    @ApiOperation("添加和修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @RequestBody Student student
    ) {
        Integer id = student.getId();
        if (null == id || id == 0) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    @ApiOperation("删除和批量删除")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("要删除的Student的id的JSON集合")
            @RequestBody List<Integer> ids
    ) {
        // 将选中的ids封装到数据库
        studentService.removeByIds(ids);

        // 返回结果
        return Result.ok();
    }
}
