package com.gngsn.map.rank.entity;

import com.gngsn.map.rank.dto.RankResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Keyword {

    @Id
    @GeneratedValue
    private Long id;

    private String word;

    private Integer hit;

    /**
     * Keyword Entity → Rank DTO
     * - 사용자에게 전달할 데이터 용 객체로 전환
     */
    public RankResult.Rank toRank() {
        return new RankResult.Rank(word, hit);
    }
}
