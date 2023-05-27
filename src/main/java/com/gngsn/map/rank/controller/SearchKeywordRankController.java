package com.gngsn.map.rank.controller;

import com.gngsn.map.rank.dto.RankResult;
import com.gngsn.map.rank.service.impl.SearchKeywordRankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gngsn.map.common.Constants.DEFAULT_LIST_COUNT;


/**
 * [v1] Search Keyword Rank Controller
 */
@RestController
@RequestMapping("/v1/map/rank")
public class SearchKeywordRankController {
    final private SearchKeywordRankService searchKeywordRankService;

    public SearchKeywordRankController(final SearchKeywordRankService searchKeywordRankService) {
        this.searchKeywordRankService = searchKeywordRankService;
    }

    /**
     * 사용자들이 많이 검색한 순서대로 최대 N개의 검색 키워드 목록 조회
     */
    @GetMapping("/search/keyword")
    public ResponseEntity<RankResult> getSearchListByKeyword() {
        return ResponseEntity.ok(searchKeywordRankService.getTopList(DEFAULT_LIST_COUNT));
    }
}
