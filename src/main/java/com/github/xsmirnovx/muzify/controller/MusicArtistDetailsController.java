package com.github.xsmirnovx.muzify.controller;

import com.github.xsmirnovx.muzify.dto.MusicArtistDetailsDTO;
import com.github.xsmirnovx.muzify.service.MusicArtistDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/musify")
public class MusicArtistDetailsController {

    private final MusicArtistDetailsService musicArtistDetailsService;

    @GetMapping(value = "/music-artist/details/{mbid}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MusicArtistDetailsDTO getMusicArtistDetails(@PathVariable UUID mbid) {
        return musicArtistDetailsService.getMusicArtistDetails(mbid);
    }
}
