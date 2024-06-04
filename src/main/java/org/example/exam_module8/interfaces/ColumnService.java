package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.Column;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ColumnService {
    void save(Column newColumn);

    Optional<Column> findById(Integer columnId);

    List<Column> getAllColumns();

    List<Column> getAllColumnsOrdered();

    Column findNextColumn(Column currentColumn);

    Optional<Column> findPreviousColumn(Column currentColumn);

    Integer findMaxColumn();

    Integer findMinColumn();

    Optional<Column> findMaxColumnItself();
}
