package com.github.xsmirnovx.muzify.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicArtistDetailsDTO {

    private UUID mbid;
    private String name;
    private String gender;
    private String country;
    private String disambiguation;
    private String description;
    private Set<AlbumDTO> albums;

    @With
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class AlbumDTO {
        private UUID id;
        private String title;
        private String imageUrl;
    }
}
