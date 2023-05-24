package com.gngsn.map.search.model.naver;

import com.gngsn.map.search.model.PlaceSearchRequest;

/**
 * Naver Map Search API Client Request.
 */
public class NaverPlaceSearchRequest implements PlaceSearchRequest {
    final private String query;
    final private int size;

    public NaverPlaceSearchRequest(String query, int size) {
        this.query = query;
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public int getSize() {
        return size;
    }
}