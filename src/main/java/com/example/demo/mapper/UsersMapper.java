package com.example.demo.mapper;

import com.example.demo.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author fll
 * @since 2020-12-29
 */
@Repository
public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Users findByUsername(String username);

}
