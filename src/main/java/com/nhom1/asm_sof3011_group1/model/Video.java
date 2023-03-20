package com.nhom1.asm_sof3011_group1.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Video {
    @Id
    private String id;
    private String title;
    private String poster;
    private Integer views;
    private String description;
    private String fileName;
    private boolean active;
    @Temporal(TemporalType.DATE)
    private Date dateUpload=new Date();
    @OneToMany(mappedBy = "video")
    List<Favorite> favorites;
    @OneToMany(mappedBy = "video")
    List<Share> shares;
}
