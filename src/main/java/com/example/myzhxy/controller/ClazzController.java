package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myzhxy.pojo.Clazz;
import com.example.myzhxy.service.ClazzService;
import com.example.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("班级管理器")
@RestController // 不是@Controller
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @ApiOperation("分页模糊查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            /**
             * 传递来的参数
             */
            @ApiParam("分页查询的页码数")
            @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页面大小")
            @PathVariable("pageSize") Integer pageSize,
            /**
             * 用 Clazz clazz 中封装的 gradeName 和 name，不用再写那么多了
             * @ApiParam("年级名称")
             * String gradeName,
             * @ApiParam("班级名称")
             * String name
             */
            @ApiParam("分页查询的条件查询") Clazz clazz
    ) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page, clazz);
        return Result.ok(iPage);
    }


    @ApiOperation("增加或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息")
            @RequestBody Clazz clazz
    ) {
        // 已封装该方法，直接调用即可
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }


    @ApiOperation("删除和批量删除")
    @RequestMapping("/deleteClazz")
    public Result deleteClazz(
            @ApiParam("要删除的所以clazz的id的JSON集合")
            @RequestBody List<Integer> ids
    ) {
        // 将选中的ids封装到数据库
        clazzService.removeByIds(ids);

        // 返回结果
        return Result.ok();
    }


    @ApiOperation("查询所以班级信息（下拉框里的内容）")
    @GetMapping("/getClazzs")
    public Result getClazzs() {
        List<Clazz> clazzs = clazzService.getClazzs();
        return Result.ok(clazzs);
    }
}
