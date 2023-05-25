package com.gngsn.map.search.model.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.PlaceSearchResponse;
import org.jsoup.Jsoup;

import java.util.List;

/**
 * Naver Map Search API Client Response.
 */
public class NaverPlaceSearchResponse implements PlaceSearchResponse {
    @JsonProperty("lastBuildDate")
    private String lastBuildDate;

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("display")
    private Integer display;

    @JsonProperty("items")
    private List<Item> items;

    /**
     * Item { title } 값을 HTML 태그 제거한 후 Place의 title로 설정
     */
    @Override
    public List<PlaceSearchResult.Place> getPlaceList() {
        return this.items.stream().map(item ->
                        new PlaceSearchResult.Place(Jsoup.parse(item.getTitle()).text()))
                .toList();
    }

    public static class Item {

        @JsonProperty("title")
        private String title;

        @JsonProperty("link")
        private String link;

        @JsonProperty("category")
        private String category;

        @JsonProperty("description")
        private String description;

        @JsonProperty("telephone")
        private String telephone;

        @JsonProperty("address")
        private String address;

        @JsonProperty("roadAddress")
        private String roadAddress;

        @JsonProperty("mapx")
        private String mapx;

        @JsonProperty("mapy")
        private String mapy;

        String getTitle() {
            return title;
        }
    }
}