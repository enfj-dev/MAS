package com.gngsn.map.search;

/**
 * Map Search Service 내에서 사용할 Constants.
 */
public class Constants {

    /**
     * Place Search System - Common Constants.
     */
    public static final int TOTAL_QUERY_SIZE = 10;
    public static final int KAKAO_QUERY_SIZE = TOTAL_QUERY_SIZE;
    public static final int DEFAULT_QUERY_SIZE = 5;

    public static final int TOO_SHORT_STRING_LENGTH = 3;
    public static final int ALLOW_COUNT_SAME_STRING = 1;

    /**
     * Place Search System - WebClient Constants.
     */
    public static final int MAX_COUNT_NO_LIMIT = -1;

    public static final int RETRY_MAX_ATTEMPTS = 3;
}
