package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Role;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.entity.enums.RoleName;
import org.example.exam_module8.interfaces.RoleService;
import org.example.exam_module8.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceIm implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public List<Role> findAllByUser(User user) {
        return roleRepo.findAllByUser(user.getId());
    }

    @Override
    public Role save(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public Role findByRole(RoleName role) {
        return roleRepo.findByRole(role);
    }
}
