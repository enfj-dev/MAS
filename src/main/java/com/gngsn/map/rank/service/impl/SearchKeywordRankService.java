package com.gngsn.map.rank.service.impl;

import com.gngsn.map.rank.dto.RankResult;
import com.gngsn.map.rank.entity.Keyword;
import com.gngsn.map.rank.repository.KeywordRepository;
import com.gngsn.map.rank.service.RankService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Search Keyword Ranking Service.
 */
@Service
public class SearchKeywordRankService implements RankService {

    final private KeywordRepository keywordRepository;

    public SearchKeywordRankService(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    /**
     * 사용자들이 많이 검색한 순서대로 최대 N개의 검색 키워드 목록 조회
     * @param n 조회할 N개의 데이터 목록 조회
     */
    @Override
    public RankResult getTopList(final int n) {
        return new RankResult(keywordRepository.findAll(PageRequest.of(0, n, Sort.by(Sort.Order.desc("hit"))))
                .stream()
                .map(Keyword::toRank)
                .toList());
    }
}
