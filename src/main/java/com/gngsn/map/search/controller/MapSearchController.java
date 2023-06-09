package com.gngsn.map.search.controller;

import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.service.PlaceSearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * [v1] Map Search Service Controller
 */
@Validated
@RestController
@RequestMapping("/v1/map/search")
public class MapSearchController {
    final private PlaceSearchService placeSearchService;

    public MapSearchController(final PlaceSearchService placeSearchService) {
        this.placeSearchService = placeSearchService;
    }

    /**
     * 다중 외부 장소 검색 API 호출 결과를 결합하여 장소 검색
     */
    @GetMapping("/place/keyword")
    public Mono<PlaceSearchResult> getSearchListByKeyword(@RequestParam @NotBlank @Size(min = 1, max = 10) final String query) {
        return placeSearchService.search(query);
    }
}
