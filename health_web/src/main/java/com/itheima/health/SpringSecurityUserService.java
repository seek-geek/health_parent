package com.itheima.health;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.itheima.health.pojo.User userInDB = userService.findUserByUsername(username);
        if (userInDB != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            Set<Role> roles = userInDB.getRoles();
            if (roles != null) {
                GrantedAuthority sga = null;
                for (Role role : roles) {
                    sga = new SimpleGrantedAuthority(role.getKeyword());
                    authorities.add(sga);
                    Set<Permission> permissions = role.getPermissions();
                    if (null != permissions) {
                        for (Permission permission : permissions) {
                            sga = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sga);
                        }
                    }
                }
            }
            User securityUser = new User(username, userInDB.getPassword(), authorities);
            return securityUser;
        }
        return null;
    }
}
