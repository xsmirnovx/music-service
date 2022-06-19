package com.github.xsmirnovx.muzify.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikidataResponseDTO {

    private Map<String, EntityDTO> entities;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityDTO {
        private Map<String, SiteLinkDTO> sitelinks;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SiteLinkDTO {
            private String title;
        }
    }
}
