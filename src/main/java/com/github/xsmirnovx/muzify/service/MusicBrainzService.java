package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.MusicBrainzClient;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MusicBrainzService {

    private final MusicBrainzClient musicBrainzClient;
    private final CircuitBreakerFactory<?, ?> cbFactory;

    public MusicBrainzResponseDTO getArtist(UUID mbid) {

        return cbFactory.create("music-brainz")
                .run(() -> musicBrainzClient.get(mbid, "json", "url-rels+release-groups"),
                        this::fallback);
    }

    public MusicBrainzResponseDTO fallback(Throwable throwable) {

        var maybeNotFound = Optional.of(throwable)
                .filter(t -> t instanceof FeignException)
                .map(FeignException.class::cast)
                .filter(ex -> ex.status() == 404);

        if (maybeNotFound.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found", throwable);
        }

        return MusicBrainzResponseDTO.builder().build();
    }
}
