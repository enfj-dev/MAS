package com.gngsn.map.search.service.impl;

import com.gngsn.map.common.cd.CacheType;
import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.model.PlaceListZipper;
import com.gngsn.map.search.model.kakao.KakaoPlaceSearchAPIClient;
import com.gngsn.map.search.model.kakao.KakaoPlaceSearchRequest;
import com.gngsn.map.search.model.naver.NaverPlaceSearchAPIClient;
import com.gngsn.map.search.model.naver.NaverPlaceSearchRequest;
import com.gngsn.map.search.service.PlaceSearchService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.gngsn.map.common.Constants.DEFAULT_QUERY_SIZE;
import static com.gngsn.map.common.Constants.KAKAO_QUERY_SIZE;

/**
 * Map Search by Keyword Service.
 */
@Service
public class KeywordPlaceSearchService implements PlaceSearchService {
    private final KakaoPlaceSearchAPIClient kakaoPlaceSearchAPIClient;
    private final NaverPlaceSearchAPIClient naverPlaceSearchAPIClient;

    public KeywordPlaceSearchService(final KakaoPlaceSearchAPIClient kakaoPlaceSearchAPIClient,
                                     final NaverPlaceSearchAPIClient naverPlaceSearchAPIClient) {
        this.kakaoPlaceSearchAPIClient = kakaoPlaceSearchAPIClient;
        this.naverPlaceSearchAPIClient = naverPlaceSearchAPIClient;
    }

    /**
     * 키워드 검색 조회
     */
    @Override
    @Cacheable(value = CacheType.Name.KEYWORD_PLACE_SEARCH, key = "#root.target + #root.methodName + '_'+ #p0")
    public Mono<PlaceSearchResult> search(final String query) {
        return getPlaceList(query).map(PlaceSearchResult::new);
    }

    /**
     * Kakao와 Naver API 응답을 취합
     * Mono.zip()는 데이터를 Tuple로 처리하기 때문에 순서 중요
     */
    private Mono<List<PlaceSearchResult.Place>> getPlaceList(final String query) {
        return Mono.zip(
                kakaoPlaceSearchAPIClient.getPlaceList(new KakaoPlaceSearchRequest(query, KAKAO_QUERY_SIZE)),
                naverPlaceSearchAPIClient.getPlaceList(new NaverPlaceSearchRequest(query, DEFAULT_QUERY_SIZE)),
                new PlaceListZipper()
        );
    }
}
