package com.github.xsmirnovx.muzify.dto;

import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoverArtResponseDTO {
    private Set<ImageDTO> images;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageDTO {
        private String image;
        private Set<String> types;
    }
}
