package org.example.exam_module8.repo;


import org.example.exam_module8.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepo extends JpaRepository<Attachment, Integer> {

    Attachment getAttachmentById(Integer attachmentId);
}
