package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.Role;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.entity.enums.RoleName;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> findAllByUser(User user);

    Role save(Role role);

    Role findByRole(RoleName role);
}
