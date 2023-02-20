package com.example.myzhxy.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.myzhxy.pojo.Admin;
import org.springframework.stereotype.Repository;

/**
 * @Repository ：识别接口。整合mybatis之后不用添加该注入也可
 *
 * BaseMapper ：继承BaseMapper类，可以实现mybatis中基本的增删改查功能
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

}
