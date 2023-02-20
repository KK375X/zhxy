package com.example.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.myzhxy.pojo.Admin;
import com.example.myzhxy.pojo.LoginForm;
import com.example.myzhxy.pojo.Student;
import com.example.myzhxy.pojo.Teacher;
import com.example.myzhxy.service.AdminService;
import com.example.myzhxy.service.StudentService;
import com.example.myzhxy.service.TeacherService;
import com.example.myzhxy.util.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 针对公共的、非增删改查功能的一些控制层代码
 */
@RestController // 不是@Controller
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired // 注入服务
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    /**
     * @RequestMapping ：可以用于post和get请求
     * @GetMapping ：只能用于get请求
     * @PostMapping ：只能用于post请求
     */


    @ApiOperation("文件上传的统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        // 设置新的文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName = uuid.concat(originalFilename.substring(i));

        // 保存文件（静态写法）（将文件发送到第三方/独立的服务器上）
        String portraitPath = "D:/MyProjects/Study/Projects/myzhxy/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 响应图片的路径
        String path = "upload/".concat(newFileName);
        return Result.ok(path);
    }


    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerifiCodeImage") // 确保浏览器可以请求到该方法
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();

        // 获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());

        // 将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);

        // 将验证码图片响应给浏览器（输出流）
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(verifiCodeImage, "JPEG", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @ApiOperation("登录的方法")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        // 验证码校验（先取出验证码在校验）
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode"); // 后台给定的验证码
        String loginVerifiCode = loginForm.getVerifiCode(); // 用户输入的验证码
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码失效，请刷新后重试！");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            // 不区分大小的的验证码判断不相等
            return Result.fail().message("验证码有误，请重试！");
        }

        // 从session域中移除验证码
        session.removeAttribute("verifiCode");

        // 准备一个map用户存放响应数据
        Map<String, Object> map = new LinkedHashMap<>();

        // 用户类型校验
        switch (loginForm.getUserType()) { // 获取用户身份
            case 1: // 管理员身份
                try {
                    Admin admin = adminService.login(loginForm);
                    if (admin != null) { // 返回的admin不是空，表面数据库里有该用户信息
                        // 将用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码错误！");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (student != null) { // 返回的admin不是空，表面数据库里有该用户信息
                        // 将用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码错误！");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (teacher != null) { // 返回的admin不是空，表面数据库里有该用户信息
                        // 将用户的类型和id转换成密文，以token的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码错误！");
                    }
                    return Result.ok(map);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此人！");
    }


    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) { // expiration如果是true，则表面token失效或者无效
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        // 从token中解析出用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map =new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin =adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student =studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher= teacherService.getByTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }
        return Result.ok(map);
    }


    /**
     * 请求参数
     *     oldPwd
     *     newPwd
     *     token 头
     * 响应的数据
     *     Result OK data= null
     */
    @ApiOperation("修改用户密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("获取token头") @RequestHeader("token") String token,
            @ApiParam("oldPwd") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("newPwd") @PathVariable("newPwd") String newPwd
    ) {
        // 校验密码是否过期（true：过期    false：失效）
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token失效
            return Result.fail().message("token失效，请重新登陆后修改密码！");
        }
        // token未失效
        // 获取用户ID和用户身份
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        // 对密码进行转换
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType) {
            case 1: // 管理员身份
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                // 利用主键找到所在位置
                queryWrapper1.eq("id", userId.intValue());
                // 对旧密码进行检验
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin != null) {// 可以进行修改
                    // 将新密码存入
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码错误，请重试！");
                }
                break;
            case 2: // 学生身份
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                // 利用主键找到所在位置
                queryWrapper2.eq("id", userId.intValue());
                // 对旧密码进行检验
                queryWrapper2.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student != null) {// 可以进行修改
                    // 将新密码存入
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码错误，请重试！");
                }
                break;
            case 3: // 教师身份
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                // 利用主键找到所在位置
                queryWrapper3.eq("id", userId.intValue());
                // 对旧密码进行检验
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher != null) {// 可以进行修改
                    // 将新密码存入
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码错误，请重试！");
                }
                break;
        }
        return Result.ok();
    }


}
