package com.github.xsmirnovx.muzify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class MusicBrainzResponseDTO {
    private String name;
    private String gender;
    private String country;
    private String disambiguation;
    private Set<RelationDTO> relations;
    @JsonProperty("release-groups")
    private Set<ReleaseGroupDTO> releaseGroups;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelationDTO {
        private String type;
        private UrlDTO url;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class UrlDTO {
            private String resource;
            private String id;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReleaseGroupDTO {
        private UUID id;
        private String title;
    }
}


