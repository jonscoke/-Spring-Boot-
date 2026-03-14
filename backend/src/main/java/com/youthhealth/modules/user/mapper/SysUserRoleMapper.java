package com.youthhealth.modules.user.mapper;

import com.youthhealth.modules.user.entity.SysUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserRoleMapper {

    @Insert("""
            INSERT INTO sys_user_role(user_id, role_id)
            VALUES(#{userId}, #{roleId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUserRole userRole);

    @Select("""
            SELECT COUNT(1)
            FROM sys_user_role
            WHERE user_id = #{userId} AND role_id = #{roleId}
            """)
    long countByUserIdAndRoleId(Long userId, Long roleId);
}
