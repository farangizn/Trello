package org.example.exam_module8.controller;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.component.CurrentUser;
import org.example.exam_module8.entity.Column;
import org.example.exam_module8.entity.Comment;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.entity.enums.PriorityName;
import org.example.exam_module8.interfaces.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {
    private final UserService userService;
    private final ColumnService columnService;
    private final TaskService taskService;
    private final CommentService commentService;
    private final CurrentUser currentUser;

    @GetMapping("/home")
    public String homePageLimited(Model model) {
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

        return "homeLimited";
    }

    @GetMapping("/task/view/{taskId}")
    private String taskPage(@PathVariable Integer taskId, Model model) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        taskOptional.ifPresent(task -> model.addAttribute("task", task));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUser", currentUser.get().get());
        PriorityName[] priorityNames = PriorityName.values();
        model.addAttribute("priorities", priorityNames);
        return "taskPageLimited";
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
        return "redirect:/user/task/view/" + taskId;
    }

}
