package com.nhom1.asm_sof3011_group1.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(playlist, video);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaylistVideoId other = (PlaylistVideoId) obj;
		return Objects.equals(playlist, other.playlist) && Objects.equals(video, other.video);
	}

    // constructors, getters, setters, etc.
}
