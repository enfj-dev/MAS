package com.gngsn.map.rank.repository;

import com.gngsn.map.rank.entity.Keyword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Keyword JPA Repository.
 */
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Override
    Page<Keyword> findAll(Pageable pageable);
}