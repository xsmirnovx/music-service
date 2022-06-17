package com.github.xsmirnovx.muzify.____service;

import com.github.xsmirnovx.muzify.client.CoverArtClient;
import com.github.xsmirnovx.muzify.dto.ArtistInfoDTO;
import com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class _CoverArtService {

    private final CoverArtClient coverArtClient;

    public Set<ArtistInfoDTO.AlbumDTO> getAlbums(Set<ArtistInfoDTO.AlbumDTO> albums) {
        var frontImages = extractFrontImages(albums);
        return io.vavr.collection.Stream.of(frontImages)
                .zipWith(albums, (img, album) -> album.withImageUrl(img))
                .toJavaSet();
    }

    private String[] extractFrontImages(Set<ArtistInfoDTO.AlbumDTO> albums) {
        return albums.parallelStream()
                .map(ArtistInfoDTO.AlbumDTO::getId)
                .map(coverArtClient::getCovers)
                .map(this::filterFrontImage)
                .toArray(String[]::new);
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
