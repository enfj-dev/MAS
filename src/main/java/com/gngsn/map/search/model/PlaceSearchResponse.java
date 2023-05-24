package com.gngsn.map.search.model;

import com.gngsn.map.search.dto.PlaceSearchResult;

import java.util.List;

/**
 * External Map Search API Client Response Interface.
 */
public interface PlaceSearchResponse {

    /**
     * 수신 받은 External API 검색 결과를 Place 객체 리스트로 변환하여 반환.
     */
    List<PlaceSearchResult.Place> getPlaceList();
}
