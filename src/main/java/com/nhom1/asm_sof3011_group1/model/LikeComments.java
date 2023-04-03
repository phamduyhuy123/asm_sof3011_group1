package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "like_comments",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"comment_id","user_id"})
})
public class LikeComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLikeCmt;
    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;
    @Column(name = "is_like")
    private boolean isLike;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "like_date")
    private LocalDateTime likeDate;
}
