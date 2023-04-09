package com.nhom1.asm_sof3011_group1.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "video")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private Long id;

    private String title;

    private String description;

    @Column(name = "upload_date")
    private Date uploadDate;

    private Long views;

    private Long duration;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "video",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes;
    @OneToMany(mappedBy = "video",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ViewHistory> viewHistories;
    // getters and setters
}
