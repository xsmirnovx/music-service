package com.github.xsmirnovx.muzify.client;

import com.github.xsmirnovx.muzify.dto.WikidataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${wikidata.api}", value = "wikidata")
public interface WikidataClient {
    @GetMapping("/{id}.json")
    WikidataResponseDTO get(@PathVariable("id") String id);
}
