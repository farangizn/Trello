package org.example.exam_module8.repo;

import org.example.exam_module8.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepo extends JpaRepository<Column, Integer> {

    @Query(value = "select max(column_order) from columns", nativeQuery = true)
    Integer findMaxColumnOrder();

    List<Column> findAllByOrderByColumnOrderAsc();

    Column findFirstByColumnOrderGreaterThanOrderByColumnOrderAsc(Integer columnOrder);

    @Query("select c from columns c where c.columnOrder < :currentOrder order by c.columnOrder desc limit 1")
    Optional<Column> findPreviousColumn(@Param("currentOrder") int currentOrder);

    @Query(value = "select min(column_order) from columns", nativeQuery = true)
    Integer findMinColumnOrder();

    @Query(value = "SELECT * FROM columns WHERE column_order = (SELECT MAX(column_order) FROM columns)", nativeQuery = true)
    Optional<Column> findMaxColumnOrderByColumnOrderAsc();

}
