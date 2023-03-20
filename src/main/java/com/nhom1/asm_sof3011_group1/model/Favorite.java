package com.nhom1.asm_sof3011_group1.model;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "videoId")
    private Video video;
    @Temporal(TemporalType.DATE)
    private Date likeDate=new Date();
}
