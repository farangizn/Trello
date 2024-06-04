package org.example.exam_module8.repo;


import org.example.exam_module8.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentContentRepo extends JpaRepository<AttachmentContent, Integer> {
    AttachmentContent getAttachmentContentByAttachmentId(Integer id);
}
