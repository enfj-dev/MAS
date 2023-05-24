package com.gngsn.map.search.model.kakao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gngsn.map.search.model.PlaceSearchRequest;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Map;

import static com.gngsn.map.search.model.SearchAPIClient.objectMapper;

/**
 * Kakao Map Search API Client Request.
 */
public class KakaoPlaceSearchRequest implements PlaceSearchRequest {
    final private String query;
    final private int size;

    public KakaoPlaceSearchRequest(String query, int size) {
        this.query = query;
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public int getSize() {
        return size;
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