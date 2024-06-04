package org.example.exam_module8.projections;

import org.example.exam_module8.entity.Task;
import org.example.exam_module8.entity.User;

import java.util.List;

public interface Report1 {
    String getUsername();
    Long getCompletedTasksCount();
    Long getLateTasksCount();
    Long getAllTasksCount();
}