package com.youthhealth.modules.user.mapper;

import com.youthhealth.modules.user.entity.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysUserMapper {

    @Select("""
            SELECT id, username, password_hash, nickname, phone, email, status, created_at, updated_at
            FROM sys_user
            WHERE username = #{username}
            """)
    @Results(id = "userResultMap", value = {
            @Result(property = "passwordHash", column = "password_hash"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    SysUser findByUsername(String username);

    @Select("""
            SELECT id, username, password_hash, nickname, phone, email, status, created_at, updated_at
            FROM sys_user
            WHERE id = #{id}
            """)
    @ResultMap("userResultMap")
    SysUser findById(Long id);

    @Insert("""
            INSERT INTO sys_user(username, password_hash, nickname, phone, email, status)
            VALUES(#{username}, #{passwordHash}, #{nickname}, #{phone}, #{email}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    @Update("""
            UPDATE sys_user
            SET nickname = #{nickname},
                phone = #{phone},
                email = #{email}
            WHERE id = #{id}
            """)
    int updateBasicInfo(SysUser user);
}
