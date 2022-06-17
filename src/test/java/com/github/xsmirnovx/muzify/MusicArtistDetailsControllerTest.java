package com.github.xsmirnovx.muzify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xsmirnovx.muzify.client.*;
import com.github.xsmirnovx.muzify.dto.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.github.xsmirnovx.muzify.dto.WikidataResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.CoverArtResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.*;
import static com.github.xsmirnovx.muzify.dto.MusicBrainzResponseDTO.RelationDTO.UrlDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MusicArtistDetailsControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private CoverArtClient coverArtClient;
    @MockBean private MusicBrainzClient musicBrainzClient;
    @MockBean private WikidataClient wikidataClient;
    @MockBean private WikipediaClient wikipediaClient;

    private final UUID testMbid = UUID.randomUUID();

    @Test
    public void test() throws Exception {
        var result = mockMvc.perform(get("/musify/music-artist/details/{mbid}", testMbid))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();
        var ai = objectMapper.readValue(content, ArtistInfoDTO.class);

        assert ai.getMbid().equals(testMbid);
        assert ai.getGender().equals("gender");
        assert ai.getCountry().equals("country");
        assert ai.getDisambiguation().equals("disambiguation");
        assert ai.getDescription().equals("description");
        assert ai.getAlbums().contains(ArtistInfoDTO.AlbumDTO.builder()
                        .title("release 2")
                        .imageUrl("front image")
                        .build());
    }

    @BeforeEach
    void setupMocks() {

        Mockito.when(musicBrainzClient.get(testMbid, "json", "url-rels+release-groups"))
                .thenReturn(MusicBrainzResponseDTO.builder()
                        .country("country")
                        .gender("gender")
                        .disambiguation("disambiguation")
                        .name("name")
                        .relations(Set.of(
                                RelationDTO.builder()
                                    .type("wikidata")
                                    .url(UrlDTO.builder().resource("http://wikidata.com/wdid").build())
                                    .build(),
                                RelationDTO.builder()
                                     .type("other")
                                     .url(UrlDTO.builder().resource("http://other.com/oid").build())
                                     .build()
                                )
                        )
                        .releaseGroups(Set.of(
                                ReleaseGroupDTO.builder()
                                    .id(UUID.randomUUID())
                                    .title("release 1")
                                    .build(),
                                ReleaseGroupDTO.builder()
                                    .id(UUID.randomUUID())
                                    .title("release 2")
                                    .build()
                                )
                        )
                        .build());

        Mockito.when(coverArtClient.getCovers(Mockito.any()))
                .thenReturn(CoverArtResponseDTO
                        .builder()
                        .images(Set.of(
                                        ImageDTO.builder()
                                                .image("front image")
                                                .types(Set.of("Front"))
                                                .build(),
                                        ImageDTO.builder()
                                                .image("back image")
                                                .types(Set.of("Back"))
                                                .build()
                                )
                        )
                        .build());

        Mockito.when(wikidataClient.get(Mockito.any()))
                .thenReturn(WikidataResponseDTO.builder()
                        .entities(Map.of("wdid", EntityDTO.builder().sitelinks(Map.of("enwiki", EntityDTO.SiteLinkDTO.builder().title("title").build())).build()))
                        .build());

        Mockito.when(wikipediaClient.getSummary(Mockito.any()))
                .thenReturn(new WikipediaResponseDTO("description"));
    }
}
