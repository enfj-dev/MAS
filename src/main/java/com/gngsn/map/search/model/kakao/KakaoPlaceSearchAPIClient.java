package com.gngsn.map.search.model.kakao;

import com.gngsn.map.search.model.SearchAPIClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * Kakao Map Search API Client.
 *
 * <a href="https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword">Reference</a>
 */
@Component
public class KakaoPlaceSearchAPIClient extends SearchAPIClient<KakaoPlaceSearchRequest> {
    final private String requestUri;
    final private String apiSecretKey;

    public KakaoPlaceSearchAPIClient(final WebClient webClient,
                                     @Value("${kakao.api.uri}") final String requestUri,
                                     @Value("${kakao.api.secret.key}") final String apiSecretKey) {
        super(webClient);
        this.requestUri = requestUri;
        this.apiSecretKey = apiSecretKey;
    }

    /**
     * Kakao API Request.
     * -
     */
    @Override
    public Mono<KakaoPlaceSearchResponse> getResponse(final KakaoPlaceSearchRequest params) {
        return this.webClient.get()
                .uri(getParamUrl(params))
                .header("Authorization", "KakaoAK " + apiSecretKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, convert4xxException())
                .onStatus(HttpStatusCode::is5xxServerError, convert5xxException())
                .bodyToMono(KakaoPlaceSearchResponse.class)
                .retryWhen(buildRetrySpec());
    }

    /**
     * KakaoPlaceSearchRequest Object → URI Parameters 변경 후 Full URI String 생성
     */
    private String getParamUrl(final KakaoPlaceSearchRequest request) {
        return UriComponentsBuilder.fromUriString(requestUri)
                .queryParams(request.toMultiValueMap())
                .build().toString();
    }
}
