package com.gngsn.map.search.controller;

import com.gngsn.map.search.config.WebClientConfiguration;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.kakao.KakaoPlaceSearchAPIClient;
import com.gngsn.map.search.model.naver.NaverPlaceSearchAPIClient;
import com.gngsn.map.search.service.impl.KeywordPlaceSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(MapSearchController.class)
@TestPropertySource(locations = "classpath:/secret.properties")
@Import({KeywordPlaceSearchService.class, KakaoPlaceSearchAPIClient.class, NaverPlaceSearchAPIClient.class, WebClientConfiguration.class})
class MapSearchControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void given_Keyword__when_HTTPRequest__then_10SizeOfPlaceList() {
        webTestClient
                .get()
                .uri("/v1/map/search/place/keyword?query=은행")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlaceSearchResult.class)
                .value(placeSearchResult -> Assertions.assertEquals(placeSearchResult.getPlaces().size(), 10));
    }
}