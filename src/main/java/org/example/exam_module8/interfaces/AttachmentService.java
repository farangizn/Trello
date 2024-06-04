package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.Attachment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public interface AttachmentService {

    Attachment getAttachmentById(Integer attachmentId);

    Attachment save(MultipartFile attachment);

    void delete(Attachment a);
}
