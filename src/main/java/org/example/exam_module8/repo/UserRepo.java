package org.example.exam_module8.repo;

import org.example.exam_module8.entity.User;
import org.example.exam_module8.projections.Report1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    @Query(value = """
            select u.username as username,
            sum(case when t.completed_at is not null then 1 else 0 end) as completedTasksCount,
            sum(case when t.deadline < t.completed_at or t.deadline < CURRENT_TIMESTAMP and t.completed_at is null then 1 else 0 end) as lateTasksCount,
            count(t.id) as allTasksCount
            from users u left join task_members tm ON u.id = tm.members_id
            left join task t ON tm.task_id = t.id
            group by u.username
            """, nativeQuery = true)
    List<Report1> report1();
}
