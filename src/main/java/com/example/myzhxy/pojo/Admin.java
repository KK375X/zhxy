package com.example.myzhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date ：自动生产get和set方法
 * @AllArgsConstructor ：有参构造方法
 * @NoArgsConstructor ：无参构造方法
 * @TableName("tb_admin") ：指定该pojo类对应的数据表
 * @TableId(value = "id", type = IdType.AUTO) ：定义主键，并规定为枚举寻找主键
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_admin")
public class Admin {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private char gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String portraitPath;    // 头像的图片路径

}
