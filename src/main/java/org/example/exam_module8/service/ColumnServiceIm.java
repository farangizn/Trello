package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Column;
import org.example.exam_module8.interfaces.ColumnService;
import org.example.exam_module8.repo.ColumnRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ColumnServiceIm implements ColumnService {

    private final ColumnRepo columnRepo;

    @Override
    public void save(Column column) {
        Integer maxOrder = columnRepo.findMaxColumnOrder();
        column.setColumnOrder(maxOrder != null ? maxOrder + 1 : 1);
        columnRepo.save(column);
    }

    public Optional<Column> findById(Integer columnId) {
        return columnRepo.findById(columnId);
    }

    @Override
    public List<Column> getAllColumns() {
        return columnRepo.findAll();
    }

    @Override
    public List<Column> getAllColumnsOrdered() {
        return columnRepo.findAllByOrderByColumnOrderAsc();
    }

    @Override
    public Column findNextColumn(Column currentColumn) {
        return columnRepo.findFirstByColumnOrderGreaterThanOrderByColumnOrderAsc(currentColumn.getColumnOrder());
    }

    @Override
    public Optional<Column> findPreviousColumn(Column currentColumn) {
        return columnRepo.findPreviousColumn(currentColumn.getColumnOrder());
    }

    @Override
    public Integer findMaxColumn() {
        return columnRepo.findMaxColumnOrder();
    }

    @Override
    public Integer findMinColumn() {
        return columnRepo.findMinColumnOrder();
    }

    @Override
    public Optional<Column> findMaxColumnItself() {
        return columnRepo.findMaxColumnOrderByColumnOrderAsc();
    }


}
