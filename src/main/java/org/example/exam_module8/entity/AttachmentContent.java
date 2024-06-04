package org.example.exam_module8.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class AttachmentContent extends BaseEntity {

    private byte[] content;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Attachment attachment;
}
