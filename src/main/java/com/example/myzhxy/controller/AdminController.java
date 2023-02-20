package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.myzhxy.pojo.Admin;
import com.example.myzhxy.service.AdminService;
import com.example.myzhxy.util.MD5;
import com.example.myzhxy.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 不是@Controller
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码号") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页面大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("搜索的关键字") String adminName
    ) {
        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        IPage<Admin> iPage = adminService.getAdminByOpr(pageParam, adminName);
        return Result.ok(iPage);
    }


    @ApiOperation("增加或者修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("以JSON格式传递数据") @RequestBody Admin admin
    ) {
        Integer id = admin.getId();
        // 如果是新增的用户，要对密码进行加密
        if (id == null || id == 0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }


    @ApiOperation("删除或批量删除")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("要删除的Admin的id的JSON集合")
            @RequestBody List<Integer> ids
    ) {
        // 将选中的ids封装到数据库
        adminService.removeByIds(ids);

        // 返回结果
        return Result.ok();
    }


}
