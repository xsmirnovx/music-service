package com.github.xsmirnovx.muzify.service;

import com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.ReleaseGroupDTO;
import static com.github.xsmirnovx.muzify.dto.ArtistInfoDTO.AlbumDTO;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = AlbumsService.class)
public class AlbumsServiceTest {

    @Autowired private AlbumsService albumsService;
    @MockBean private CoverArtService coverArtService;

    private final UUID id1 = UUID.randomUUID();
    private final UUID id2 = UUID.randomUUID();

    @BeforeEach
    void setupMocks() {
        Mockito.when(coverArtService.getFrontImage(id1))
                .thenReturn(CompletableFuture.completedFuture("image 1"));
        Mockito.when(coverArtService.getFrontImage(id2))
                .thenReturn(CompletableFuture.completedFuture("image 2"));
    }

    @Test
    public void testGetAlbums() throws Exception {

        var response = MusicBrainzResponseDTO.builder().releaseGroups(
            Set.of(
                 ReleaseGroupDTO.builder().title("release 1").id(id1).build(),
                 ReleaseGroupDTO.builder().title("release 2").id(id2).build()
            )
        )
        .build();

        var res = albumsService.getAlbums(response);

        var actualAlbums= res.get().stream()
                .sorted(Comparator.comparing(AlbumDTO::getId))
                .collect(Collectors.toUnmodifiableSet());


        var expectedAlbums = Stream
                .of(
                        AlbumDTO.builder().title("release 1").imageUrl("image 1").id(id1).build(),
                        AlbumDTO.builder().title("release 2").imageUrl("image 2").id(id2).build()
                )
                .sorted(Comparator.comparing(AlbumDTO::getId))
                .collect(Collectors.toUnmodifiableSet());

        assertIterableEquals(actualAlbums, expectedAlbums);
    }
}
