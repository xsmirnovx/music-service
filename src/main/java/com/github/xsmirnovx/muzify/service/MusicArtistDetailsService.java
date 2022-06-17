package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.dto.ArtistInfoDTO;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicArtistDetailsService {

    private final MusicBrainzService musicBrainzService;
    private final WikipediaService wikipediaService;
    private final AlbumsService albumsService;

    public ArtistInfoDTO getMusicArtistDetails(UUID mbid) {

        var musicBrainzResponse = musicBrainzService.getArtist(mbid);
        var futureWikiExtract = wikipediaService.getArtistDescription(musicBrainzResponse);
        var futureAlbumsWithImages = albumsService.getAlbums(musicBrainzResponse);

        CompletableFuture.allOf(futureWikiExtract, futureAlbumsWithImages).join();

        var wikiExtract = Try.of(futureWikiExtract::get).getOrElse("<failed to get description>");
        var albumsWithImages = Try.of(futureAlbumsWithImages::get).getOrElse(Set::of);

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
}
