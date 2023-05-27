package com.gngsn.map.rank.dto;

import java.util.List;

/**
 * 다중 장소 검색 API 호출 후 결합한 결과 데이터 및 사용자 반환 DTO
 */
public class RankResult {
    private List<Rank> ranking;

    public RankResult() {
    }

    public RankResult(final List<Rank> ranking) {
        this.ranking = ranking;
    }

    public List<Rank> getRanking() {
        return ranking;
    }

    /**
     * 다중 장소 검색 API 호출 후 응답 데이터들을 결합할 공통 형식.
     */
    static public class Rank {
        private String keyword;
        private int count;

        public Rank() {
        }

        public Rank(final String keyword, final int count) {
            this.keyword = keyword;
            this.count = count;
        }

        public String getKeyword() {
            return keyword;
        }

        public int getCount() {
            return count;
        }
    }
}
