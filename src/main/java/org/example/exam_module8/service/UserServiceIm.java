package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.interfaces.UserService;
import org.example.exam_module8.projections.Report1;
import org.example.exam_module8.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceIm implements UserService {

    private final UserRepo userRepo;

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<Report1> report1() {
        return userRepo.report1();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepo.findById(id);
    }


}
