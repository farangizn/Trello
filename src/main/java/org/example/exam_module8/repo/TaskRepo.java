package org.example.exam_module8.repo;

import jakarta.transaction.Transactional;
import org.example.exam_module8.entity.Column;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.projections.Report2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {

    @Query(value = "select * from task t join task_members tm on t.id = tm.task_id where tm.members_id = :userId", nativeQuery = true)
    List<Task> findAllByUser(Integer userId);

    @Query(value = "select c.name as ColumnName, count(t.id) as NumberOfTasks " +
            "from columns c " +
            "left join task t on c.id = t.column_id " +
            "group by c.name",
            nativeQuery = true)
    List<Report2> countByColumn();

    @Query(value = "select * from task t left join task_members tm on t.id = tm.task_id where tm.members_id is null", nativeQuery = true)
    List<Task> findAllByNoUser();

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.column = :column WHERE t = :task")
    void changeTaskColumn(@Param("task") Task task, @Param("column") Column column);


    List<Task> findAllByColumn(Column column1);
}
