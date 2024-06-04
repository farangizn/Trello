package org.example.exam_module8.interfaces;

import org.example.exam_module8.entity.AttachmentContent;
import org.springframework.stereotype.Service;


@Service
public interface AttachmentContentService {
    AttachmentContent getAttachmentContentByAttachmentId(Integer id);

    void save(AttachmentContent ac);

    void delete(AttachmentContent ac);
}
