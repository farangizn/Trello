package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.Comment;
import org.springframework.stereotype.Service;


@Service
public interface CommentService {
    void add(Comment comment);
}
