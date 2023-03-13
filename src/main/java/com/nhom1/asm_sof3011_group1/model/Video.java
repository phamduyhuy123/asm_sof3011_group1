package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue
    private String id;
    private String title;
    private String poster;
    private Integer views;
    private String description;
    private boolean active;
}
