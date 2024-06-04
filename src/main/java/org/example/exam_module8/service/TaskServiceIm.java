package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Column;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.interfaces.ColumnService;
import org.example.exam_module8.interfaces.TaskService;
import org.example.exam_module8.projections.Report2;
import org.example.exam_module8.repo.TaskRepo;
import org.example.exam_module8.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceIm implements TaskService {
    private final TaskRepo taskRepo;
    private final ColumnService columnService;

    @Override
    public void save(Task task) {
        taskRepo.save(task);
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Optional<Task> findById(Integer taskId) {
        return taskRepo.findById(taskId);
    }

    @Override
    public void editTaskColumn(Task task, int i) {
        if (i == 1) {
            Column currentColumn = task.getColumn();
            Integer maxColumn = columnService.findMaxColumn();
            Column nextColumn = columnService.findNextColumn(currentColumn);

            if (nextColumn != null && Objects.equals(nextColumn.getColumnOrder(), maxColumn)) {

                task.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));

                if (task.getDeadline() != null && task.getCompletedAt().toLocalDateTime().isAfter(task.getDeadline().toLocalDateTime())) {
                    task.setPastDeadline(true);
                } else {
                    task.setPastDeadline(false);
                }
                taskRepo.save(task);
            }

            if (maxColumn != null && nextColumn != null && nextColumn.getColumnOrder() <= maxColumn) {
                task.setColumn(nextColumn);
                taskRepo.save(task);
            }
        } else if (i == -1) {
            Column currentColumn = task.getColumn();
            Integer minColumn = columnService.findMinColumn();
            Integer maxColumn = columnService.findMaxColumn();
            Optional<Column> previousColumn = columnService.findPreviousColumn(currentColumn);
            if (previousColumn.isPresent() && minColumn != null && previousColumn.get().getColumnOrder() >= minColumn) {
                if (currentColumn.getColumnOrder().equals(maxColumn)) {
                    task.setCompletedAt(null);
                }
                task.setColumn(previousColumn.get());
                taskRepo.save(task);

            }
        }

    }

    @Override
    public List<Task> findAllByUser(User currentUser) {
        return taskRepo.findAllByUser(currentUser.getId());
    }

    @Override
    public List<Report2> report2() {
        return taskRepo.countByColumn();
    }

    @Override
    public List<Task> findAllByNoUser() {
        return taskRepo.findAllByNoUser();
    }

    @Override
    public void changeTaskCOlumn(Integer taskId, Integer newColumnId) {
        Optional<Task> taskOptional = taskRepo.findById(taskId);
        Optional<Column> columnOptional = columnService.findById(newColumnId);
        if (taskOptional.isPresent() && columnOptional.isPresent()) {
            Task task = taskOptional.get();
            Column column = columnOptional.get();
            taskRepo.changeTaskColumn(task, column);

        }
    }

    @Override
    public List<Task> findAllByColumn(Column column1) {
        return taskRepo.findAllByColumn(column1);
    }

}
