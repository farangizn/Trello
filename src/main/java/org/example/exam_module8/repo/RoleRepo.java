package org.example.exam_module8.repo;

import org.example.exam_module8.entity.Role;
import org.example.exam_module8.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    @Query(
            nativeQuery = true,
            value = """
                    select r.* from roles r join users_roles ur on r.id = ur.roles_id
                    where ur.users_id = ?1
            """)
    List<Role> findAllByUser(Integer id);

    Role findByRole(RoleName role);
}
