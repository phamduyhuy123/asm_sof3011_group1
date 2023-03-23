package com.nhom1.asm_sof3011_group1.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class ViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;


    private LocalDateTime viewDate;

    // constructors, getters, setters, etc.
}
