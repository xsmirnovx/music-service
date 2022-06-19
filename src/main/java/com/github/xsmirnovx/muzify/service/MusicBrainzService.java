package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.MusicBrainzClient;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MusicBrainzService {

    private final MusicBrainzClient musicBrainzClient;
    private final CircuitBreakerFactory<?, ?> cbFactory;

    public MusicBrainzResponseDTO getArtist(UUID mbid) {
        return cbFactory.create("music-brainz")
                .run(() -> musicBrainzClient.get(mbid, "json", "url-rels+release-groups"),
                        throwable -> MusicBrainzResponseDTO.builder().build());
    }
}
