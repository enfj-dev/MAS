package com.gngsn.map.common.cd;

public enum CacheType {

    SEARCH_KEYWORD_RANK(Name.SEARCH_KEYWORD_RANK, 5L),
    KEYWORD_PLACE_SEARCH(Name.KEYWORD_PLACE_SEARCH, 60L);

    private final String cacheName;
    private final long expireSeconds;

    CacheType(final String cacheName, final long expireSeconds) {
        this.cacheName = cacheName;
        this.expireSeconds = expireSeconds;
    }

    public String getCacheName() {
        return cacheName;
    }

    public long getExpireSeconds() {
        return expireSeconds;
    }

    public static class Name {
        public static final String SEARCH_KEYWORD_RANK = "searchKeywordRank";
        public static final String KEYWORD_PLACE_SEARCH = "keywordPlaceSearch";
    }
}
