package org.example.exam_module8.config;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Role;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.interfaces.RoleService;
import org.example.exam_module8.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByUsername(username);
    }
}
