package com.gngsn.map.search.dto;

import java.util.List;

/**
 * 다중 장소 검색 API 호출 후 통합한 결과 데이터이자, 사용자 반환 DTO
 */
public class PlaceSearchResult {
    private List<Place> places;

    public PlaceSearchResult() {
    }

    public PlaceSearchResult(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    /**
     * 다중 장소 검색 API 호출 후 응답 데이터들을 통합할 공통 형식.
     */
    static public class Place {
        private String title;

        public Place() {
        }

        public Place(final String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
