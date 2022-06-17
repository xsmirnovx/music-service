package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.dto.ArtistInfoDTO.*;
import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumsService {

    private final CoverArtService coverArtService;

    @Async
    public CompletableFuture<Set<AlbumDTO>> getAlbums(MusicBrainzResponseDTO response) {
        var albums = extractAlbums(response);
        var albumsWithImages = getAlbumsWithImages(albums);
//        var albumsWithImages = Stream.ofAll(images.stream())
//                .zipWith(albums, (img, album) -> album.withImageUrl(img))
//                .toJavaSet();

        return CompletableFuture.completedFuture(albumsWithImages);
    }

    private Set<AlbumDTO> extractAlbums(MusicBrainzResponseDTO response) {
        return Optional.of(response.getReleaseGroups())
                .stream()
                .flatMap(Collection::stream)
                .map(group -> AlbumDTO.builder()
                        .id(group.getId())
                        .title(group.getTitle())
                        .build())
                .collect(Collectors.toUnmodifiableSet());
    }

    private Set<AlbumDTO> getAlbumsWithImages(Set<AlbumDTO> albums) {

        var futureImages = albums.stream()
                //.map(AlbumDTO::getId)
                .map(album -> Tuple.of(album, coverArtService.getFrontImage(album.getId())))
                .collect(Collectors.toUnmodifiableSet());

        CompletableFuture
                .allOf(futureImages.stream().map(Tuple2::_2).collect(Collectors.toUnmodifiableSet())
                        .toArray(new CompletableFuture<?>[] {})).join();

        return futureImages.stream()
                .map(t -> t._1.withImageUrl(getImageOrStubMessage(t._2)))
                .collect(Collectors.toUnmodifiableSet());
    }

    private String getImageOrStubMessage(CompletableFuture<String> futureImage) {
        return Try.of(futureImage::get).getOrElse("<image not found>");
    }
}
