package org.example.exam_module8.config;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Column;
import org.example.exam_module8.entity.Role;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.entity.enums.PriorityName;
import org.example.exam_module8.entity.enums.RoleName;
import org.example.exam_module8.interfaces.ColumnService;
import org.example.exam_module8.interfaces.RoleService;
import org.example.exam_module8.interfaces.TaskService;
import org.example.exam_module8.interfaces.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final ColumnService columnService;
    private final TaskService taskService;

    @Override
    public void run(String... args) throws Exception {
        generateData();
    }

    private void generateData() {

        Role roleUser = new Role(RoleName.ROLE_USER);
        roleService.save(roleUser);

        Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
        roleService.save(roleAdmin);

        User user1 = new User(
                "a",
                passwordEncoder.encode("1"),
                "Ansor",
                "Nasriddinov"
        );
        user1.setEmail("f8rangiz@gmail.com");
        user1.setRoles(List.of(roleAdmin));

        User user2 = new User(
                "hikmat",
                passwordEncoder.encode("1"),
                "Hikmat",
                "Hikmatov"
        );
        user2.setEmail("f8rangiz@gmail.com");
        user2.setRoles(List.of(roleUser));

        User user3 = new User(
                "nusrat1",
                passwordEncoder.encode("1"),
                "Nusrat",
                "Nusratov"
        );
        user3.setEmail("f8rangiz@gmail.com");
        user3.setRoles(List.of(roleUser));

        User user4 = new User(
                "tosh",
                passwordEncoder.encode("1"),
                "Toshmat",
                "Toshmatov"
        );
        user4.setEmail("f8rangiz@gmail.com");
        user4.setRoles(List.of(roleUser));

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);

        Column column1 = new Column();
        column1.setName("Created");

        Column column2 = new Column();
        column2.setName("In Progress");

        Column column3 = new Column();
        column3.setName("Completed");

        columnService.save(column1);
        columnService.save(column2);
        columnService.save(column3);

        Task task1 = new Task();
        task1.setTitle("Configure spring security");
        task1.setDescription("Add a configuration of spring security with custom userDetails, login, register pages");
        task1.setDeadline(Timestamp.valueOf(LocalDateTime.of(2024, 7, 14, 12, 1)));
        task1.setColumn(column1);

        task1.setPriority(PriorityName.MEDIUM.toString());

        Task task2 = new Task();
        task2.setTitle("Create home page");
        task2.setDescription("Create a professionally looking homepage with user and order table CRUD operations. There should be a header with logo too");
        task2.setDeadline(Timestamp.valueOf(LocalDateTime.now()));
        task2.setColumn(column2);

        task2.setPriority(PriorityName.LOW.toString());

        Task task3 = new Task();
        task3.setTitle("Create tables in database");
        task3.setDescription("Create all the necessary tables in PostgreSql, use JPA. All the tables should have logical connections");
        task3.setDeadline(Timestamp.valueOf(LocalDateTime.of(2024, 7, 14, 12, 1)));
        task3.setColumn(column3);
        task3.setPriority(PriorityName.HIGH.toString());

        Task task4 = new Task();
        task4.setTitle("Make the code clean");
        task4.setDescription("Go through the whole project and clean the code troubleshooting errors (if any)");
        task4.setDeadline(Timestamp.valueOf(LocalDateTime.of(2024, 7, 14, 12, 1)));
        task4.setColumn(column1);
        task4.setPriority(PriorityName.LOW.toString());

        taskService.save(task1);
        taskService.save(task2);
        taskService.save(task3);
        taskService.save(task4);
    }
}
