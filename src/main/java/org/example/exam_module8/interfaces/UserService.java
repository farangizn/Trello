package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.User;
import org.example.exam_module8.projections.Report1;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface UserService {
    User findByUsername(String username);

    User save(User user);

    Optional<User> findById(Integer id);

    List<User> findAll();

    List<Report1> report1();
}
