package org.example.exam_module8.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exam_module8.component.CurrentUser;
import org.example.exam_module8.entity.*;
import org.example.exam_module8.entity.enums.PriorityName;
import org.example.exam_module8.entity.enums.RoleName;
import org.example.exam_module8.interfaces.*;
import org.example.exam_module8.projections.Report1;
import org.example.exam_module8.projections.Report2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ColumnService columnService;

    private final CommentService commentService;
    private final CurrentUser currentUser;
    private final AttachmentService attachmentService;
    private final TaskService taskService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/calendar")
    public String calendarPage(Model model) {
        model.addAttribute("tasks", taskService.findAllTasks());
        return "calendar";
    }

    @GetMapping("/task/view/{id}")
    public String calendarPage(@PathVariable Integer id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        taskOptional.ifPresent(task -> model.addAttribute("task", task));
        return "taskPageLimited";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        Role roleUser = roleService.findByRole(RoleName.ROLE_USER);

        User regUser = User.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(List.of(roleUser))
                .build();

        userService.save(regUser);

        return "login";
    }

    @PostMapping("/success")
    public String success() {
        Optional<User> user = currentUser.get();
        if (user.isPresent()) {
            if (user.get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
                return "redirect:/user/home";
            } else if (user.get().getRoles().contains(roleService.findByRole(RoleName.ROLE_ADMIN))) {
                return "redirect:/home";
            }
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "logout";
    }

    @PostMapping("/home")
    public String homePage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        List<Column> columns = columnService.getAllColumnsOrdered();
        List<Task> tasks = taskService.findAllTasks();

        for (Task task : tasks) {
            if (!columnService.findMaxColumn().equals(task.getColumn().getColumnOrder()) && task.getDeadline() != null) {
                task.setPastDeadline(task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now()));
                taskService.save(task);
            }
        }
        if (columnService.findMaxColumn() != null) {
            model.addAttribute("maxColumn", columnService.findMaxColumn());

        }
        model.addAttribute("columns", columns);
        model.addAttribute("tasks", tasks);

        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }

        return "home";
    }

    @PostMapping("/{taskId}/move-to-column/{newColumnId}")
    public ResponseEntity<?> moveTaskToColumn(@PathVariable Integer taskId, @PathVariable Integer newColumnId) {
        taskService.changeTaskCOlumn(taskId, newColumnId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/table")
    public String tablePage(Model model) {
        List<Column> columns = columnService.getAllColumnsOrdered();
        List<Task> tasks = taskService.findAllTasks();

        for (Task task : tasks) {
            if (!columnService.findMaxColumn().equals(task.getColumn().getColumnOrder()) && task.getDeadline() != null) {
                task.setPastDeadline(task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now()));
                taskService.save(task);
            } else {
                task.setPastDeadline(false);
                taskService.save(task);
            }
        }

        model.addAttribute("columns", columns);
        model.addAttribute("tasks", tasks);

        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }

        return "table";
    }

    @PostMapping("/addColumn")
    public String addColumn(@RequestParam("columnName") String columnName) {
        if (!Objects.equals(columnName, "")) {
            Column column = new Column();
            column.setName(columnName);
            columnService.save(column);
        }
        return "redirect:/home";
    }

    @PostMapping("/addTask")
    public String addTask(@Valid @RequestParam("columnId") Integer columnId, @RequestParam("title") String taskTitle) {

        Task task = new Task();

        if (!Objects.equals(taskTitle, "")) {
            task.setTitle(taskTitle);
            Optional<Column> columnOptional = columnService.findById(columnId);
            if (columnOptional.isPresent()) {
                Column column = columnOptional.get();
                task.setColumn(column);
                if (column.getColumnOrder().equals(columnService.findMaxColumn())) {
                    task.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
                }
                taskService.save(task);
            }
        }
        return "redirect:/home";
    }

    @PostMapping("/updateTaskColumn")
    public String updateTaskColumn(@RequestParam("taskId") Integer taskId, @RequestParam("columnId") Integer columnId) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Optional<Column> columnOptional = columnService.findById(columnId);
            if (columnOptional.isPresent()) {
                Column column = columnOptional.get();
                task.setColumn(column);
                taskService.save(task);
            }
        }
        return "redirect:/home";
    }

    @GetMapping("/updateColumnOrder/{columnId}/{direction}")
    public String updateColumnOrder(@PathVariable("columnId") Integer columnId, @PathVariable Integer direction) {
        Optional<Column> columnOptional = columnService.findById(columnId);
        if (columnOptional.isPresent()) {
            Column column = columnOptional.get();
            List<Column> allColumnsOrdered = columnService.getAllColumnsOrdered();

            int currentIndex = allColumnsOrdered.indexOf(column);

            int totalColumns = allColumnsOrdered.size();
            int nextIndex;
            if (direction == 1) {
                nextIndex = (currentIndex + 1) % totalColumns;
            } else {
                nextIndex = (currentIndex - 1 + totalColumns) % totalColumns;
            }

            allColumnsOrdered.remove(column);

            allColumnsOrdered.add(nextIndex, column);

            for (int i = 0; i < allColumnsOrdered.size(); i++) {
                allColumnsOrdered.get(i).setColumnOrder(i);
            }

            for (Column updatedColumn : allColumnsOrdered) {
                columnService.save(updatedColumn);
            }

//            for (Task task : taskService.findAllTasks()) {
//                if (task.getColumn().getColumnOrder().equals(columnService.findMaxColumn())) {
//                    task.setPastDeadline(false);
//                }
//            }
        }
        return "redirect:/home";
    }


    @GetMapping("/task/column/frontEdit/{taskId}")
    private String editTaskOrderPlus(@PathVariable Integer taskId) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskService.editTaskColumn(task, 1);
        }
        return "redirect:/home";
    }

    @GetMapping("/task/column/backEdit/{taskId}")
    private String editTaskOrderMinus(@PathVariable Integer taskId) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            taskService.editTaskColumn(task, -1);
        }
        return "redirect:/home";
    }

    @GetMapping("/task/edit/{taskId}")
    private String editTaskPage(@PathVariable Integer taskId, Model model) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        taskOptional.ifPresent(task -> model.addAttribute("task", task));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUser", currentUser.get().get());
        PriorityName[] priorityNames = PriorityName.values();
        model.addAttribute("priorities", priorityNames);
        return "taskPage";
    }

    @PostMapping("/task/title/update/{id}")
    public String updateTaskTitle(@PathVariable Integer id, @RequestParam("title") String title) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setTitle(title);
            taskService.save(task);
        }
        return "redirect:/task/edit/" + id;
    }

    @PostMapping("/task/description/update/{id}")
    public String updateTaskDescription(@PathVariable Integer id, @RequestParam("description") String description) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setDescription(description);
            taskService.save(task);
        }
        return "redirect:/task/edit/" + id;
    }

    @PostMapping("/task/priority/update/{id}")
    public String updateTaskPriority(@PathVariable Integer id, @RequestParam("priority") String priority) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setPriority(PriorityName.valueOf(priority).toString());
            taskService.save(task);
        }
        return "redirect:/task/edit/" + id;
    }

    @PostMapping("/task/deadline/update/{id}")
    public String updateTaskDeadline(@PathVariable Integer id, @RequestParam("deadline") String deadline) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            LocalDateTime parsedDeadline = LocalDateTime.parse(deadline);
            task.setDeadline(Timestamp.valueOf(parsedDeadline));
            taskService.save(task);
        }
        return "redirect:/task/edit/" + id;
    }

    @PostMapping("/task/members/update/{id}")
    public String updateTaskMembers(@PathVariable Integer id, @RequestParam("userId") Integer userId) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                task.getMembers().add(user);
                taskService.save(task);
            }
        }
        return "redirect:/task/edit/" + id;
    }

    @GetMapping("/task/member/remove/{taskId}/{memberId}")
    public String removeTaskMember(@PathVariable Integer taskId, @PathVariable Integer memberId) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Optional<User> userOptional = userService.findById(memberId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                task.getMembers().remove(user);
                taskService.save(task);
            }
        }
        return "redirect:/task/edit/" + taskId;
    }

    @PostMapping("/task/comment/add/{taskId}")
    public String addComment(@PathVariable Integer taskId, @RequestParam("commentContent") String commentContent) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Comment comment = new Comment(
                    commentContent,
                    currentUser.get().orElseThrow(),
                    Timestamp.valueOf(LocalDateTime.now())
            );
            commentService.add(comment);
            task.getComments().add(comment);
            taskService.save(task);
        }
        return "redirect:/task/edit/" + taskId;
    }

    @GetMapping("/task/filter/me")
    private String filterMe(Model model) {
        List<Task> tasks = taskService.findAllByUser(currentUser.get().get());

        for (Task task : tasks) {
            if (!columnService.findMaxColumn().equals(task.getColumn().getColumnOrder()) && task.getDeadline() != null) {
                task.setPastDeadline(task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now()));
                taskService.save(task);
            } else {
                task.setPastDeadline(false);
                taskService.save(task);
            }
        }

        model.addAttribute("currentTasks", tasks);
        List<Column> columns = columnService.getAllColumnsOrdered();
        model.addAttribute("columns", columns);


        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }


        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "homeLimited";
        } else {
            return "home";
        }
    }

    @GetMapping("/task/filter/none")
    private String filterNone(Model model) {
        List<Task> tasks = taskService.findAllByNoUser();
        for (Task task : tasks) {
            if (!columnService.findMaxColumn().equals(task.getColumn().getColumnOrder()) && task.getDeadline() != null) {
                task.setPastDeadline(task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now()));
                taskService.save(task);
            } else {
                task.setPastDeadline(false);
                taskService.save(task);
            }
        }
        model.addAttribute("currentTasks", tasks);
        List<Column> columns = columnService.getAllColumnsOrdered();
        model.addAttribute("columns", columns);
        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }


        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "homeLimited";
        } else {
            return "home";
        }
    }

    @GetMapping("/task/filter/all")
    private String filterAll(Model model) {
        List<Task> tasks = taskService.findAllTasks();
        model.addAttribute("currentTasks", tasks);
        List<Column> columns = columnService.getAllColumnsOrdered();
        model.addAttribute("columns", columns);

        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }
        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "homeLimited";
        } else {
            return "home";
        }
    }

    @GetMapping("/task/filter/late")
    private String filterLate(Model model) {
        List<Task> tasks = new ArrayList<>();

        for (Task task : taskService.findAllTasks()) {
            if (task.getCompletedAt() == null && task.getDeadline() != null && task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now())) {
                tasks.add(task);
            }
        }

        for (Task task : tasks) {
            if (!columnService.findMaxColumn().equals(task.getColumn().getColumnOrder()) && task.getDeadline() != null) {
                task.setPastDeadline(task.getDeadline().toLocalDateTime().isBefore(LocalDateTime.now()));
                taskService.save(task);
            } else {
                task.setPastDeadline(false);
                taskService.save(task);
            }
        }

        model.addAttribute("currentTasks", tasks);
        List<Column> columns = columnService.getAllColumnsOrdered();
        model.addAttribute("columns", columns);


        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }


        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "homeLimited";
        } else {
            return "home";
        }
    }

    @GetMapping("/task/filter/late/day")
    private String filterLateIn24(Model model) {

        List<Task> tasks = new ArrayList<>();
//        List<Task> tasks = taskService.findAllByNoUser();

        LocalDateTime now = LocalDateTime.now();
        for (Task task : taskService.findAllTasks()) {
            if (task.getCompletedAt() == null && task.getDeadline() != null && task.getDeadline().toLocalDateTime().isBefore(now.plusHours(24)) && task.getPastDeadline() != null && !task.getPastDeadline()) {
                tasks.add(task);
            }
        }

        model.addAttribute("currentTasks", tasks);
        List<Column> columns = columnService.getAllColumnsOrdered();
        model.addAttribute("columns", columns);


        if (currentUser.get().isPresent()) {
            model.addAttribute("currentUser", currentUser.get().get());
        }


        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "homeLimited";
        } else {
            return "home";
        }
    }

    @GetMapping("/task/report/columns")
    private String columnTaskReportPage(Model model) {
        List<Report2> report2s = taskService.report2();
        model.addAttribute("reportAttributes", report2s);
        for (Report2 report2 : report2s) {
            System.out.println(report2.getColumnName());
            System.out.println(report2.getNumberOfTasks());
        }
        return "columnReport";
    }

    @GetMapping("/task/report/status")
    private String statusTaskReportPage(Model model) {
        List<Report1> report1s = userService.report1();
        model.addAttribute("reportAttributes", report1s);
        return "statusReport";
    }

    @GetMapping("/profile/settings")
    private String profileSettingsPage(Model model, HttpSession session) {
        if (currentUser.get().isPresent()) {
            User user = currentUser.get().get();
            model.addAttribute("currentUser", user);
        }
        Integer attachmentId = (Integer) session.getAttribute("attachmentId");
        model.addAttribute("attachmentId", attachmentId);
        return "profile";
    }

    @PostMapping("/profile/settings")
    public String updateProfileSettings(@RequestParam("username") String username,
                                        @RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        HttpServletRequest request,
                                        Model model) {
        if (currentUser.get().isPresent()) {
            User user = currentUser.get().get();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            HttpSession session = request.getSession();
            Integer attachmentId = (Integer) session.getAttribute("attachmentId");
            System.out.println(attachmentId);
            Attachment attachment = attachmentService.getAttachmentById(attachmentId);
            user.setPhoto(attachment);
            userService.save(user);

        }

        if (currentUser.get().get().getRoles().contains(roleService.findByRole(RoleName.ROLE_USER))) {
            return "redirect:/user/home";
        } else {
            return "redirect:/home";
        }
    }
}