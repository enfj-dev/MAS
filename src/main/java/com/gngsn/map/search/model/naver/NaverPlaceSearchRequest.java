package com.gngsn.map.search.model.naver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gngsn.map.search.model.PlaceSearchRequest;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

import static com.gngsn.map.search.model.SearchAPIClient.objectMapper;

/**
 * Naver Map Search API Client Request.
 */
public class NaverPlaceSearchRequest implements PlaceSearchRequest {
    final private String query;
    final private int display; // max =5
    final private int start = 1;
    final private String sort = "random"; // random | comment

    public NaverPlaceSearchRequest(String query, int display) {
        this.query = query;
        this.display = display;
    }

    public String getQuery() {
        return query;
    }

    public int getDisplay() {
        return display;
    }

    /**
     * this Object → MultiValueMap 변환
     */
    public LinkedMultiValueMap<String, String> toMultiValueMap() {
        return new LinkedMultiValueMap<>() {{
            setAll(convertToMap());
        }};
    }

    /**
     * this Object → Map 변환
     */
    private Map<String, String> convertToMap() {
        return objectMapper.convertValue(this, new TypeReference<Map<String, String>>() {
        });
    }
}