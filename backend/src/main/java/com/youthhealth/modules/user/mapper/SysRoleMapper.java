package com.youthhealth.modules.user.mapper;

import com.youthhealth.modules.user.entity.SysRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Select("""
            SELECT id, role_code, role_name, status, created_at, updated_at
            FROM sys_role
            WHERE role_code = #{roleCode}
            """)
    SysRole findByRoleCode(String roleCode);

    @Insert("""
            INSERT INTO sys_role(role_code, role_name, status)
            VALUES(#{roleCode}, #{roleName}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysRole role);

    @Select("""
            SELECT r.role_code
            FROM sys_role r
            JOIN sys_user_role ur ON r.id = ur.role_id
            WHERE ur.user_id = #{userId}
            ORDER BY r.id
            """)
    List<String> findRoleCodesByUserId(Long userId);
}
