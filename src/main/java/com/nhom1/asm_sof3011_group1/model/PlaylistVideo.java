package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

@IdClass(PlaylistVideoId.class)
@Table(name = "playlist_video",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"position","playlist_id"})})
public class PlaylistVideo {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "position")
    private int position;

    // getters and setters
}