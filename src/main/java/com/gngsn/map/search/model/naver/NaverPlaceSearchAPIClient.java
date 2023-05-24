package com.gngsn.map.search.model.naver;

import com.gngsn.map.search.model.SearchAPIClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * Naver Map Search API Client.
 */
@Component
public class NaverPlaceSearchAPIClient extends SearchAPIClient<NaverPlaceSearchRequest> {

    public NaverPlaceSearchAPIClient(WebClient webClient) {
        super(webClient);
    }

    @Override
    public Mono<NaverPlaceSearchResponse> getResponse(NaverPlaceSearchRequest params) {
        // TODO
        return Mono.just(new NaverPlaceSearchResponse());
    }
}