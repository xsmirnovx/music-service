package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.client.CoverArtClient;
import com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoverArtService {

    private static final String IMAGE_NOT_FOUND = "<image not found>";
    private final CoverArtClient coverArtClient;

    @Async
    public CompletableFuture<String> getFrontImage(UUID mbid) {

        var frontImage = tryGetCoverArt(mbid)
                .map(this::filterFrontImage)
                .orElse(IMAGE_NOT_FOUND);

        return CompletableFuture.completedFuture(frontImage);
    }

    private Optional<CoverArtResponseDTO> tryGetCoverArt(UUID mbid) {
        return Try.ofSupplier(() -> coverArtClient.getCovers(mbid)).toJavaOptional();
    }

    private String filterFrontImage(CoverArtResponseDTO response) {
        return response.getImages().stream()
                .filter(r -> Objects.nonNull(r.getTypes()))
                .filter(r -> r.getTypes().contains("Front"))
                .map(ImageDTO::getImage)
                .findFirst()
                .orElse(IMAGE_NOT_FOUND);
    }
}
