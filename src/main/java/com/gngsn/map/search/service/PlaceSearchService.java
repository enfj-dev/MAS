package com.gngsn.map.search.service;

import com.gngsn.map.search.dto.PlaceSearchResult;
import reactor.core.publisher.Mono;

/**
 * Map Search Service.
 */
public interface PlaceSearchService {

    /**
     * 키워드 검색 조회
     */
    Mono<PlaceSearchResult> search(String query);
}
