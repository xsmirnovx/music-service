package com.github.xsmirnovx.muzify.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikipediaResponseDTO {
    @JsonProperty("extract_html") private String extract;
}
