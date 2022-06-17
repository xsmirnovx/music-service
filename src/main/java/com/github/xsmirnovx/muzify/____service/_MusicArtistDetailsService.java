package com.github.xsmirnovx.muzify.____service;

import com.github.xsmirnovx.muzify.dto.ArtistInfoDTO;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import com.github.xsmirnovx.muzify.service.MusicBrainzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.RelationDTO.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class _MusicArtistDetailsService {

    //private final CircuitBreakerFactory cbFactory;
    private final MusicBrainzService musicBrainzService;
    private final _CoverArtService coverArtService;
    private final _WikidataService wikidataService;
    private final _WikipediaService wikipediaService;

    public ArtistInfoDTO getMusicArtistDetails(UUID mbid) {

        var musicBrainzResponse = musicBrainzService.getArtist(mbid);
        var wikiExtract = getWikiExtract(musicBrainzResponse);
        var albumsWithImages = getAlbumsWithImages(musicBrainzResponse);

        return ArtistInfoDTO
                .builder()
                .mbid(mbid)
                .name(musicBrainzResponse.getName())
                .gender(musicBrainzResponse.getGender())
                .country(musicBrainzResponse.getCountry())
                .disambiguation(musicBrainzResponse.getDisambiguation())
                .description(wikiExtract)
                .albums(albumsWithImages)
                .build();
    }

    private String getWikiExtract(MusicBrainzResponseDTO response) {
        var wikidataUrl = extractWikidataUrl(response);
        var title = wikidataService.getTitle(wikidataUrl.get()); // fixme
        return wikipediaService.getExtract(title.get()); // fixme
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

    private Set<ArtistInfoDTO.AlbumDTO> getAlbumsWithImages(MusicBrainzResponseDTO response) {
        var albums = extractAlbums(response);
        return coverArtService.getAlbums(albums);
    }

    private Set<ArtistInfoDTO.AlbumDTO> extractAlbums(MusicBrainzResponseDTO response) {
        return Optional.of(response.getReleaseGroups())
                .stream()
                .flatMap(Collection::stream)
                .map(group -> ArtistInfoDTO.AlbumDTO.builder()
                        .id(group.getId())
                        .title(group.getTitle())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    private Optional<String> extractIdFromWikidataUrl(String wikidataUrl) {
        return Arrays.stream(wikidataUrl.split("/")).reduce((str1, str2) -> str2);
    }
}
