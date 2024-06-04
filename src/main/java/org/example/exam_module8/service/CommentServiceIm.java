package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Comment;
import org.example.exam_module8.interfaces.CommentService;
import org.example.exam_module8.repo.CommentRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceIm implements CommentService {

    private final CommentRepo commentRepo;

    @Override
    public void add(Comment comment) {
        commentRepo.save(comment);
    }
}
