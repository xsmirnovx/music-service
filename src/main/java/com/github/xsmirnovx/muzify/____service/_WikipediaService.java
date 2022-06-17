package com.github.xsmirnovx.muzify.____service;

import com.github.xsmirnovx.muzify.client.WikipediaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class _WikipediaService {

    private final WikipediaClient wikipediaClient;

    public String getExtract(String title) {
        return wikipediaClient.getSummary(title).getExtract(); // todo: error handling
    }

}
