package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.MusicBrainzClient;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MusicBrainzService {

    private final MusicBrainzClient musicBrainzClient;

    public MusicBrainzResponseDTO getArtist(UUID mbid) {
        return musicBrainzClient.get(mbid, "json", "url-rels+release-groups");
    }
}
