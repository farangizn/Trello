package org.example.exam_module8.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.userDetailsService(customUserDetailsService);

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(m -> {
            m
                    .requestMatchers("/login", "/register","/css/**", "/")
                    .permitAll()
                    .requestMatchers("/addTask",  "/home", "/addColumn",
                            "/updateTaskColumn", "/updateColumnOrder/*", "/task/column/*",
                            "/task/column/backEdit/*", "/task/edit/*", "/task/title/update/*",
                            "/task/description/update/", "/task/priority/update/", "/task/deadline/update/",
                            "/task/members/update/", "/task/member/remove/")
                    .hasRole("ADMIN")
                    .requestMatchers("/user/*").hasRole("USER")
                    .requestMatchers("/calendar", "/task/view/*",
                            "/success", "/logout", "/table",
                            "/task/comment/add/", "/task/filter/me",
                            "/task/filter/all", "/task/filter/none",
                            "/task/filter/late", "/task/filter/late/day",
                            "/task/report/columns", "/task/report/status",
                            "/profile/settings", "/file/*").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated();
        });

        http.formLogin(m -> {
            m.loginPage("/login")
                    .successForwardUrl("/success");
        });

        http.logout(m -> {
            m.logoutUrl("/logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
        });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
}
