package com.github.xsmirnovx.muzify.client;

import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(url = "${musicbrainz.api}", value = "music-brainz")
public interface MusicBrainzClient {
    @GetMapping(value = "/artist/{id}", produces = APPLICATION_JSON_VALUE)
    MusicBrainzResponseDTO get(
            @PathVariable("id") UUID id,
            @RequestParam("fmt") String fmt,
            @RequestParam("inc") String inc);
}
