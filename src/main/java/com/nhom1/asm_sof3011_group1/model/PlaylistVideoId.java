package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

@Embeddable
public class PlaylistVideoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "playlist_id")
    private Long playlist;

    @Column(name = "video_id")
    private Long video;

    // constructors, getters, setters, etc.
}
