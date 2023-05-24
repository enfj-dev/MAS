package com.gngsn.map.search.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gngsn.map.search.dto.PlaceSearchResult;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.gngsn.map.search.Constants.RETRY_MAX_ATTEMPTS;

/**
 * 외부 Map Search API Client.
 * @param <T> request type per external services.
 */
public abstract class SearchAPIClient<T extends PlaceSearchRequest> {
    public static final ObjectMapper objectMapper = new ObjectMapper();
    protected final WebClient webClient;

    protected SearchAPIClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 각 외부 API 검색 결과 별, Place 객체 리스트로 변환하여 반환.
     */
    public Mono<List<PlaceSearchResult.Place>> getPlaceList(final T params) {
        return getResponse(params).map(PlaceSearchResponse::getPlaceList);
    }

    /**
     * 외부 API 검색 HTTP 요청
     */
    protected abstract Mono<? extends PlaceSearchResponse> getResponse(T params);

    /**
     * 외부 API 검색 HTTP 요청 시, 5xx 오류가 발생할 경우 예외 처리 집중화
     */
    protected Function<ClientResponse, Mono<? extends Throwable>> convert5xxException() {
        return clientResponse -> {
            throw new RuntimeException(clientResponse.bodyToMono(String.class).toString());
        };
    }

    /**
     * 외부 API 검색 HTTP 요청 시, 4xx 오류가 발생할 경우 예외 처리 집중화
     */
    protected Function<ClientResponse, Mono<? extends Throwable>> convert4xxException() {
        return clientResponse -> {
            throw new IllegalArgumentException(clientResponse.bodyToMono(String.class).toString());
        };
    }

    /**
     * Backoff 적용한 Webclient Request Retry Strategy.
     * - maxAttempts = 3: 최초 시도를 포함 3번 재시도 + backoff 지수 2초
     * - jitter: 임의의 난수(계산된 지연의 N%) default 0.5
     * - Retry 모두 실패 시 RuntimeException
     */
    protected RetryBackoffSpec buildRetrySpec() {
        return Retry
                .backoff(RETRY_MAX_ATTEMPTS, Duration.ofSeconds(2L))
                .filter(throwable -> throwable instanceof Exception)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new RuntimeException(String.format("키워드 장소 검색 API Retry %s회 실패. 데이터 확인 후 조치 필요: ", RETRY_MAX_ATTEMPTS));
                });
    }
}
