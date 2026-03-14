package com.youthhealth.security;

import com.youthhealth.modules.user.entity.SysUser;
import com.youthhealth.modules.user.mapper.SysRoleMapper;
import com.youthhealth.modules.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<String> roles = sysRoleMapper.findRoleCodesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(roleCode -> new SimpleGrantedAuthority("ROLE_" + roleCode))
                .toList();
        return new AuthUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getStatus() == 1,
                authorities
        );
    }
}
