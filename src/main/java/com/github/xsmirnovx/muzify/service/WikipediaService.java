package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.WikidataClient;
import com.github.xsmirnovx.muzify.client.WikipediaClient;
import com.github.xsmirnovx.muzify.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.RelationDTO.*;
import static com.github.xsmirnovx.muzify.dto.WikidataResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.WikidataResponseDTO.EntityDTO.*;

@Service
@RequiredArgsConstructor
public class WikipediaService {

    private final WikipediaClient wikipediaClient;
    private final WikidataClient wikidataClient;

    private final CircuitBreakerFactory<?, ?> cbFactory;

    @Async
    public CompletableFuture<String> getArtistDescription(MusicBrainzResponseDTO response) {
        var wikiExtract = extractWikidataUrl(response)
                .flatMap(this::getTitleFromWikidata)
                .map(this::getWikiExtract)
                .orElse("<cannot get description>");

        return CompletableFuture.completedFuture(wikiExtract);
    }

    private Optional<String> extractWikidataUrl(MusicBrainzResponseDTO response) {
        return Optional.ofNullable(response.getRelations()).stream()
                .flatMap(Collection::stream)
                .filter(r -> "wikidata".equals(r.getType()))
                .map(RelationDTO::getUrl)
                .map(UrlDTO::getResource)
                .map(this::extractIdFromWikidataUrl)
                .flatMap(Optional::stream)
                .findFirst();
    }

    private Optional<String> extractIdFromWikidataUrl(String wikidataUrl) {
        return Arrays.stream(wikidataUrl.split("/")).reduce((str1, str2) -> str2);
    }

    private Optional<String> getTitleFromWikidata(String id) {

        var response = cbFactory.create("wikidata")
                .run(() -> wikidataClient.get(id), throwable -> WikidataResponseDTO.builder().build());

        return Optional.ofNullable(response.getEntities())
                .map(e -> e.get(id))
                .map(EntityDTO::getSitelinks)
                .map(e -> e.get("enwiki"))
                .map(SiteLinkDTO::getTitle);
    }

    private String getWikiExtract(String title) {
        return cbFactory.create("wikipedia").run(() -> wikipediaClient.getSummary(title),
                throwable -> WikipediaResponseDTO.builder().build()).getExtract();
    }
}
