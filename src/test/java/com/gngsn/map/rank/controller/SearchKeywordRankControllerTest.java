package com.gngsn.map.rank.controller;

import com.gngsn.map.rank.dto.RankResult;
import com.gngsn.map.rank.service.impl.SearchKeywordRankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.IntStream;

@DisplayName("최대 10개의 인기 검색 키워드 목록 조회 기능 테스트")
@WebFluxTest(SearchKeywordRankController.class)
@Import({SearchKeywordRankService.class})
@AutoConfigureDataJpa
class SearchKeywordRankControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("인기 검색어 목록은 count 순으로 내림차순 정렬")
    public void test_OrderedRankingList() {
        webTestClient
                .get()
                .uri("/v1/map/rank/search/keyword")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RankResult.class)
                .value(placeSearchResult -> {
                    final List<RankResult.Rank> ranking = placeSearchResult.getRanking();
                    Assertions.assertTrue(
                            // 다음 Rank count 보다 작은 건이 없어야 함 (다음 count 보다 항상 커야 함)
                            IntStream.range(0, ranking.size() - 1)
                                    .filter(i -> ranking.get(i).getCount() < ranking.get(i + 1).getCount())
                                    .findAny()
                                    .isEmpty());
                });
    }
}