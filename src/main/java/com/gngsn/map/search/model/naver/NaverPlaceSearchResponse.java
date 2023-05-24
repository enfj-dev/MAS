package com.gngsn.map.search.model.naver;

import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.PlaceSearchResponse;

import java.util.List;

/**
 * Naver Map Search API Client Response.
 */
public class NaverPlaceSearchResponse implements PlaceSearchResponse {

    /**
     * TODO: 수신 받은 Naver API 검색 결과를 Place 객체 리스트로 변환하여 반환.
     */
    @Override
    public List<PlaceSearchResult.Place> getPlaceList() {
        return List.of(new PlaceSearchResult.Place("naver1"),
                new PlaceSearchResult.Place("naver2"),
                new PlaceSearchResult.Place("naver3"),
                new PlaceSearchResult.Place("naver4"),
                new PlaceSearchResult.Place("naver5"));
    }
}
