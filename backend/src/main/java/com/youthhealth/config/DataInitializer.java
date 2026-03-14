package com.youthhealth.config;

import com.youthhealth.modules.user.entity.SysRole;
import com.youthhealth.modules.user.entity.SysUser;
import com.youthhealth.modules.user.entity.SysUserRole;
import com.youthhealth.modules.user.mapper.SysRoleMapper;
import com.youthhealth.modules.user.mapper.SysUserMapper;
import com.youthhealth.modules.user.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysRoleMapper sysRoleMapper;
    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String... args) {
        SysRole adminRole = ensureRole("ADMIN", "管理员");
        ensureRole("USER", "普通用户");

        SysUser admin = sysUserMapper.findByUsername("admin");
        if (admin == null) {
            admin = new SysUser();
            admin.setUsername("admin");
            admin.setNickname("系统管理员");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123456"));
            admin.setStatus(1);
            admin.setPhone("13800000000");
            admin.setEmail("admin@youthhealth.local");
            sysUserMapper.insert(admin);
        }
        if (sysUserRoleMapper.countByUserIdAndRoleId(admin.getId(), adminRole.getId()) == 0) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(admin.getId());
            userRole.setRoleId(adminRole.getId());
            sysUserRoleMapper.insert(userRole);
        }
    }

    private SysRole ensureRole(String roleCode, String roleName) {
        SysRole role = sysRoleMapper.findByRoleCode(roleCode);
        if (role != null) {
            return role;
        }
        role = new SysRole();
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setStatus(1);
        sysRoleMapper.insert(role);
        return role;
    }
}
