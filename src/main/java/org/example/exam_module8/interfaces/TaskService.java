package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.Column;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.entity.User;
import org.example.exam_module8.projections.Report2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface TaskService {

    void save(Task task);

    List<Task> findAllTasks();

    Optional<Task> findById(Integer taskId);

    void editTaskColumn(Task task, int i);

    List<Task> findAllByUser(User currentUser);

    List<Report2> report2();

    List<Task> findAllByNoUser();

    void changeTaskCOlumn(Integer taskId, Integer newColumnId);

    List<Task> findAllByColumn(Column column1);
}
