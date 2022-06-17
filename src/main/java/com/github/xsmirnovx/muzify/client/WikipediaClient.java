package com.github.xsmirnovx.muzify.client;

import com.github.xsmirnovx.muzify.dto.WikipediaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(url = "${wikipedia.api}", value = "wikipedia")
public interface WikipediaClient {

    @GetMapping(value = "/{title}", produces = APPLICATION_JSON_VALUE)
    WikipediaResponseDTO getSummary(@PathVariable String title);
}
