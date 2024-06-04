package org.example.exam_module8.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.exam_module8.entity.Attachment;
import org.example.exam_module8.entity.AttachmentContent;
import org.example.exam_module8.entity.Task;
import org.example.exam_module8.interfaces.AttachmentContentService;
import org.example.exam_module8.interfaces.AttachmentService;
import org.example.exam_module8.interfaces.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentContentService attachmentContentService;
    private final TaskService taskService;

    @GetMapping("{id}")
    private void load(HttpServletResponse response, @PathVariable Integer id) {
        try {
            AttachmentContent ac = attachmentContentService.getAttachmentContentByAttachmentId(id);
            Attachment attachment = attachmentService.getAttachmentById(id);
            response.setContentType(attachment.getContentType());
            response.getOutputStream().write(ac.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    private String upload(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        String ct = file.getContentType();
//        String contentType = ct.substring(ct.lastIndexOf("/") + 1);
        AttachmentContent ac = AttachmentContent.builder()
                .attachment(
                        Attachment.builder()
                                .contentType(ct)
                                .build()
                )
                .content(file.getInputStream().readAllBytes())
                .build();
        attachmentContentService.save(ac);
        session.setAttribute("attachmentId", ac.getAttachment().getId());

        return "redirect:/profile/settings";
    }

    @PostMapping("{taskId}")
    private String upload(@RequestParam MultipartFile file, HttpSession session, @PathVariable  Integer taskId) throws IOException {
        String ct = file.getContentType();
        AttachmentContent ac = AttachmentContent.builder()
                .attachment(
                        Attachment.builder()
                                .contentType(ct)
                                .build()
                )
                .content(file.getInputStream().readAllBytes())
                .build();
        attachmentContentService.save(ac);

        Attachment attachment = attachmentService.getAttachmentById(ac.getAttachment().getId());
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.getAttachments().add(attachment);
            taskService.save(task);
        }

        session.setAttribute("attachmentId", ac.getAttachment().getId());

        return "redirect:/task/edit/" + taskId;
    }

    @GetMapping("/download/{attachmentId}")
    private void download(HttpServletResponse response, @PathVariable Integer attachmentId) {
        AttachmentContent ac = attachmentContentService.getAttachmentContentByAttachmentId(attachmentId);
        Attachment a = attachmentService.getAttachmentById(attachmentId);
        response.addHeader("Content-Disposition", "attachment;filename=file." + a.getContentType().substring(a.getContentType().lastIndexOf("/") + 1)); // download qilinib turgan qileni nomini berib qo'yamiz
        response.setContentType("application/octet-stream");

        try {
            response.getOutputStream().write(ac.getContent());
            response.getOutputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/remove/{attachmentId}/{taskId}")
    private String remove(HttpServletResponse response, @PathVariable Integer attachmentId, @PathVariable Integer taskId) {
        Optional<Task> taskOptional = taskService.findById(taskId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            List<Attachment> taskAttachments = task.getAttachments();
            if (taskAttachments != null) {
                Iterator<Attachment> iterator = taskAttachments.iterator();
                while (iterator.hasNext()) {
                    Attachment attachment = iterator.next();
                    if (attachment.getId() != null && attachment.getId().equals(attachmentId)) {
                        iterator.remove(); // Use iterator to safely remove the attachment
                        taskService.save(task);
                        break; // Break after removing the attachment to avoid further iteration
                    }
                }
            }
        }
        return "redirect:/task/edit/" + taskId;
    }
}
