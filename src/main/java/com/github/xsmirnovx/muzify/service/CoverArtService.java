package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.CoverArtClient;
import com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO;
import io.vavr.Function1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoverArtService {

    private final CoverArtClient coverArtClient;

    @Async
    public CompletableFuture<String> getImage(UUID mbid) {
        return Function1.of(coverArtClient::getCovers)
                .andThen(this::filterFrontImage)
                .andThen(CompletableFuture::completedFuture)
                .apply(mbid);
    }

    private String filterFrontImage(CoverArtResponseDTO response) {
        return response.getImages().stream()
                .filter(r -> Objects.nonNull(r.getTypes()))
                .filter(r -> r.getTypes().contains("Front"))
                .map(CoverArtResponseDTO.ImageDTO::getImage)
                .findFirst()
                .orElse("<image not found>");
    }
}
