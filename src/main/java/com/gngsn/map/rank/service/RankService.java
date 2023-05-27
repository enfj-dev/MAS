package com.gngsn.map.rank.service;

import com.gngsn.map.rank.dto.RankResult;

/**
 * Ranking Service Interface.
 */
public interface RankService {

    /**
     * 상위 N개의 목록 조회
     * @param n 조회할 상위 N개의 데이터 목록 조회
     */
    RankResult getTopList(int n);
}
