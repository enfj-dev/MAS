package com.gngsn.map.search.model.naver;

import com.gngsn.map.search.model.SearchAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Naver Map Search API Client.
 *
 * <a href="https://developers.naver.com/docs/serviceapi/search/local/local.md#%EC%A7%80%EC%97%AD-%EA%B2%80%EC%83%89-api-%EB%A0%88%ED%8D%BC%EB%9F%B0%EC%8A%A4">Reference</a>
 */
@Component
public class NaverPlaceSearchAPIClient extends SearchAPIClient<NaverPlaceSearchRequest> {
    final private String requestUri;
    final private String apiClientId;
    final private String apiClientSecret;

    NaverPlaceSearchAPIClient(final WebClient webClient,
                              @Value("${naver.api.uri}") final String requestUri,
                              @Value("${naver.api.client.id}") final String apiClientId,
                              @Value("${naver.api.client.secret}") final String apiClientSecret) {
        super(webClient);
        this.requestUri = requestUri;
        this.apiClientId = apiClientId;
        this.apiClientSecret = apiClientSecret;
    }

    @Override
    public Mono<NaverPlaceSearchResponse> getResponse(final NaverPlaceSearchRequest params) {
        return this.webClient.get()
                .uri(getParamUrl(params))
                .header("X-Naver-Client-Id", apiClientId)
                .header("X-Naver-Client-Secret", apiClientSecret)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, convert4xxException())
                .onStatus(HttpStatusCode::is5xxServerError, convert5xxException())
                .bodyToMono(NaverPlaceSearchResponse.class)
                .retryWhen(buildRetrySpec())
                .onErrorResume(errorHandler());
    }

    /**
     * Error Handler
     */
    private Function<Throwable, Mono<? extends NaverPlaceSearchResponse>> errorHandler() {
        return throwable -> {
            throw new RuntimeException(throwable);
        };
    }

    /**
     * NaverPlaceSearchRequest Object → URI Parameters 변경 후 Full URI String 생성
     */
    private String getParamUrl(final NaverPlaceSearchRequest request) {
        return UriComponentsBuilder.fromUriString(requestUri)
                .queryParams(request.toMultiValueMap())
                .build().toString();
    }
}