package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playlist_id")
    private Long id;

    private String name;

    private String description;

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
}