package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.WikidataClient;
import com.github.xsmirnovx.muzify.client.WikipediaClient;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import com.github.xsmirnovx.muzify.dto.WikipediaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.RelationDTO.*;

@Service
@RequiredArgsConstructor
public class WikipediaService {

    private final WikipediaClient wikipediaClient;
    private final WikidataClient wikidataClient;

    @Async
    public CompletableFuture<String> getArtistDescription(MusicBrainzResponseDTO response) {
        var wikiExtract = extractWikidataUrl(response)
                .flatMap(this::getTitleFromWikidata)
                .map(wikipediaClient::getSummary)
                .map(WikipediaResponseDTO::getExtract)
                .orElse("<cannot get description>");

        return CompletableFuture.completedFuture(wikiExtract);
    }

    private Optional<String> extractWikidataUrl(MusicBrainzResponseDTO response) {
        return response.getRelations().stream()
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

        var title = wikidataClient.get(id)
                .getEntities()
                .get(id)
                .getSitelinks()
                .get("enwiki")
                .getTitle();

        return Optional.ofNullable(title);
    }
}
