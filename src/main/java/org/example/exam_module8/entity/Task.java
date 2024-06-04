package org.example.exam_module8.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Task extends BaseEntity {
    private String title;
    private String description;
    private Timestamp deadline;
    private Timestamp completedAt;
    private String priority;
    private Boolean pastDeadline;
    @ManyToMany
    private List<User> members;

    @OneToMany
    private List<Comment> comments;

    @ManyToMany
    List<Attachment> attachments;

    @ManyToOne
    private Column column;

}
