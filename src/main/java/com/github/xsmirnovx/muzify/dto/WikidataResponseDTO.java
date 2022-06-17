package com.github.xsmirnovx.muzify.dto;

import lombok.*;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikidataResponseDTO {

    private Map<String, EntityDTO> entities;

//    public Optional<Map<String, EntityDTO>> getEntities() {
//        return Optional.ofNullable(entities);
//    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityDTO {
        private Map<String, SiteLinkDTO> sitelinks;

//        public Optional<Map<String, SiteLinkDTO>> getSitelinks() {
//            return Optional.ofNullable(sitelinks);
//        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SiteLinkDTO {
            private String title;

//            public Optional<String> getTitle() {
//                return Optional.ofNullable(title);
//            }
        }
    }
}
