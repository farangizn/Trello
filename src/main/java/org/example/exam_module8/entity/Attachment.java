package org.example.exam_module8.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Attachment extends BaseEntity {
    private String name;
    private String contentType;
    private Integer size;
}
