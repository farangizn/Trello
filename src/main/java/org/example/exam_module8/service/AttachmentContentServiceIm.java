package org.example.exam_module8.service;

import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.AttachmentContent;
import org.example.exam_module8.interfaces.AttachmentContentService;
import org.example.exam_module8.repo.AttachmentContentRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentContentServiceIm implements AttachmentContentService {

    private final AttachmentContentRepo attachmentContentRepo;

    @Override
    public AttachmentContent getAttachmentContentByAttachmentId(Integer id) {
        return attachmentContentRepo.getAttachmentContentByAttachmentId(id);
    }

    @Override
    public void save(AttachmentContent ac) {
        attachmentContentRepo.save(ac);
    }

    @Override
    public void delete(AttachmentContent ac) {
        attachmentContentRepo.delete(ac);
    }
}
