package com.gngsn.map.search.controller;

import com.gngsn.map.common.config.WebClientConfiguration;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.kakao.KakaoPlaceSearchAPIClient;
import com.gngsn.map.search.model.naver.NaverPlaceSearchAPIClient;
import com.gngsn.map.search.service.impl.KeywordPlaceSearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@DisplayName("외부 장소 검색 API 기반의 장소 검색 서비스 테스트")
@WebFluxTest(MapSearchController.class)
@TestPropertySource(locations = "classpath:/secret.properties")
@Import({KeywordPlaceSearchService.class, KakaoPlaceSearchAPIClient.class, NaverPlaceSearchAPIClient.class, WebClientConfiguration.class})
class MapSearchControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("'카카오' 검색은 10개의 리스트를 출력")
    public void given_Keyword__when_HTTPRequest__then_10SizeOfPlaceList() {
        webTestClient
                .get()
                .uri("/v1/map/search/place/keyword?query=카카오")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlaceSearchResult.class)
                .value(placeSearchResult -> Assertions.assertEquals(placeSearchResult.getPlaces().size(), 10));
    }
}