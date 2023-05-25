package com.gngsn.map.search.model.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.PlaceSearchResponse;

import java.util.List;


/**
 * Kakao Map Search API Client Response.
 */
public class KakaoPlaceSearchResponse implements PlaceSearchResponse {
    private Meta meta;
    private List<Document> documents;

    public KakaoPlaceSearchResponse() {
    }

    public KakaoPlaceSearchResponse(final Meta meta, final List<Document> documents) {
        this.meta = meta;
        this.documents = documents;
    }

    /**
     * 수신 받은 Kakao API 검색 결과를 Place 객체 리스트로 변환하여 반환.
     */
    @Override
    public List<PlaceSearchResult.Place> getPlaceList() {
        return this.documents.stream().map(document -> new PlaceSearchResult.Place(document.getPlaceName())).toList();
    }

    public List<Document> getDocuments() {
        return this.documents;
    }

    public static class Meta {

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("totalCount")
        private int pageableCount;

        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("same_name")
        private SameName sameName;
    }

    public static class SameName {
        @JsonProperty("region")
        private String[] region;

        @JsonProperty("keyword")
        private String keyword;

        @JsonProperty("selected_region")
        private String selectedRegion;
    }

    public static class Document {
        @JsonProperty("id")
        private String id;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("category_name")
        private String categoryName;

        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        @JsonProperty("category_group_name")
        private String categoryGroupName;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        @JsonProperty("keyword")
        private String keyword;

        @JsonProperty("x")
        private String x;

        @JsonProperty("y")
        private String y;

        @JsonProperty("place_url")
        private String placeUrl;

        @JsonProperty("distance")
        private String distance;

        public String getPlaceName() {
            return this.placeName;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }
    }
}