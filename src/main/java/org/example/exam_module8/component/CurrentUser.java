package org.example.exam_module8.component;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrentUser {

    public Optional<User> get() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return Optional.of((User) principal);
            }
        }

        return Optional.empty();
    }
}
