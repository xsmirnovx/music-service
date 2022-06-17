package com.github.xsmirnovx.muzify.____service;

import com.github.xsmirnovx.muzify.client.WikidataClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class _WikidataService {
    private final WikidataClient wikidataClient;

    public Optional<String> getTitle(String id) {

        var title = wikidataClient.get(id)
                .getEntities()
                .get(id)
                .getSitelinks()
                .get("enwiki")
                .getTitle();

        return Optional.ofNullable(title); // fixme
    }
}
