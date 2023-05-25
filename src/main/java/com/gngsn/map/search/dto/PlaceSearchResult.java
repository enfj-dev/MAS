package com.gngsn.map.search.dto;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.List;
import java.util.Objects;

import static com.gngsn.map.search.Constants.ALLOW_COUNT_SAME_STRING;
import static com.gngsn.map.search.Constants.TOO_SHORT_STRING_LENGTH;

/**
 * 다중 장소 검색 API 호출 후 결합한 결과 데이터 및 사용자 반환 DTO
 */
public class PlaceSearchResult {
    private List<Place> places;

    public PlaceSearchResult() {
    }

    public PlaceSearchResult(final List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    /**
     * 다중 장소 검색 API 호출 후 응답 데이터들을 결합할 공통 형식.
     */
    static public class Place implements Comparable<Place> {
        private String title;

        public Place() {
        }

        public Place(final String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        /**
         * 속성 값 'title'을 기반으로 Place 객체 비교
         *
         * @param place the object to be compared.
         */
        @Override
        public int compareTo(final Place place) {
            return this.getTitle().compareTo(place.getTitle());
        }

        /**
         * 속성 값 'title' 문자열 유사성 판단
         *
         * @param place the object to be compared.
         */
        public boolean isSame(final Place place) {
            final int allowableNumber = isTooShort(place) ? 0 : ALLOW_COUNT_SAME_STRING;
            return getSimilarityNumber(this.getTitle(), place.getTitle()) <= allowableNumber;
        }

        /**
         * 장소 문자열의 길이가 너무 짧은 경우 판단
         */
        private boolean isTooShort(final Place place) {
            return this.getTitle().length() <= TOO_SHORT_STRING_LENGTH || place.getTitle().length() <= TOO_SHORT_STRING_LENGTH;
        }

        /**
         * 공백 제거한 두 문자열 값의 유사도 수치 계산
         */
        private Integer getSimilarityNumber(final String source, final String target) {
            return new LevenshteinDistance().apply(
                    source.replace("\\s+", ""),
                    target.replace("\\s+", ""));
        }

        /**
         * Place 객체의 기본 비교 값을 title로 한정
         */
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final Place place = (Place) o;
            return Objects.equals(title, place.title);
        }

        /**
         * Place 객체의 기본 비교 값을 title로 한정
         */
        @Override
        public int hashCode() {
            return Objects.hash(title);
        }
    }
}
