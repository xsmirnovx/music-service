package com.github.xsmirnovx.muzify.client;

import com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(url = "${coverart.api}", value = "cover-art")
public interface CoverArtClient {

    @GetMapping("/release-group/{id}")
    CoverArtResponseDTO getCovers(@PathVariable("id") UUID id);
}
