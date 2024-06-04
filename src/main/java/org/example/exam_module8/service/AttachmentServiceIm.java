package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Attachment;
import org.example.exam_module8.interfaces.AttachmentService;
import org.example.exam_module8.repo.AttachmentRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AttachmentServiceIm implements AttachmentService {

    private final AttachmentRepo attachmentRepo;

    @Override
    public Attachment getAttachmentById(Integer attachmentId) {
        return attachmentRepo.getAttachmentById(attachmentId);
    }

    @Override
    public Attachment save(MultipartFile attachment) {
        return null;
    }

    @Override
    public void delete(Attachment a) {
        attachmentRepo.delete(a);
    }
}
