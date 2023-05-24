package com.gngsn.map.search;

/**
 * Map Search Service 내에서 사용할 Constants.
 */
public class Constants {

    /**
     * API URI Constants.
     */
    public static final String KAKAO_SEARCH_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";
    public static final String NAVER_SEARCH_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    public static final int QUERY_SIZE = 5;

    /**
     * WebClient Constants.
     */
    public static final int MAX_COUNT_NO_LIMIT = -1;

    public static final int RETRY_MAX_ATTEMPTS = 3;

}
