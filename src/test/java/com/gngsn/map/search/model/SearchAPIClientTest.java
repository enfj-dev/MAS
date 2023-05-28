package com.gngsn.map.search.model;

import com.gngsn.map.search.dto.PlaceSearchResult;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import com.gngsn.map.search.exception.SearchAPIExternalServerException;
import com.gngsn.map.search.exception.WebClientRetryExhaustedException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.gngsn.map.common.Constants.RETRY_MAX_ATTEMPTS;
import static com.gngsn.map.search.model.SearchAPIClient.objectMapper;

@DisplayName("External API 요청 시 WebClient Exception Handling 테스트")
public class SearchAPIClientTest {

    private MockWebServer mockBackEnd;

    private MockResponse MOCK_RESPONSE_4XX;

    private MockResponse MOCK_RESPONSE_5XX;

    @BeforeEach
    public void setupMockServer() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        final List<PlaceSearchResult.Place> MOCK_RESPONSE_BODY = List.of(new PlaceSearchResult.Place("Error Test"));
        MOCK_RESPONSE_4XX = new MockResponse().setResponseCode(400).setBody(objectMapper.writeValueAsString(MOCK_RESPONSE_BODY));
        MOCK_RESPONSE_5XX = new MockResponse().setResponseCode(500).setBody(objectMapper.writeValueAsString(MOCK_RESPONSE_BODY));
    }

    @Test
    @DisplayName("External API 응답이 4xx Client 오류면 SearchAPIBadRequestException 오류를 던짐")
    void getPlaceList_4xxError_SearchAPIBadRequestException() {
        final SearchAPIClient<TestPlaceSearchRequest> clientErrorClient = new SearchAPIClient<>(getWebClient("test/4xx")) {
            @Override
            protected Mono<TestPlaceSearchResponse> getResponse(final TestPlaceSearchRequest params) {
                return webClient.get()
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, convert4xxException())
                        .onStatus(HttpStatusCode::is5xxServerError, convert5xxException())
                        .bodyToMono(TestPlaceSearchResponse.class);
            }
        };

        mockBackEnd.enqueue(MOCK_RESPONSE_4XX);

        final Mono<List<PlaceSearchResult.Place>> monoErrorTest = clientErrorClient.getPlaceList(new TestPlaceSearchRequest());

        StepVerifier
                .create(monoErrorTest)
                .expectError(SearchAPIBadRequestException.class)
                .verify();
    }

    @Test
    @DisplayName("External API 응답이 5xx Client 오류면 SearchAPIExternalServerException 오류를 던짐")
    void getPlaceList_5xxError_SearchAPIExternalServerException() {
        final SearchAPIClient<TestPlaceSearchRequest> serverErrorClient = new SearchAPIClient<>(getWebClient("test/5xx")) {
            @Override
            protected Mono<SearchAPIClientTest.TestPlaceSearchResponse> getResponse(final TestPlaceSearchRequest params) {
                return webClient.get()
                        .retrieve()
                        .onStatus(HttpStatusCode::is4xxClientError, convert4xxException())
                        .onStatus(HttpStatusCode::is5xxServerError, convert5xxException())
                        .bodyToMono(SearchAPIClientTest.TestPlaceSearchResponse.class);
            }
        };

        mockBackEnd.enqueue(MOCK_RESPONSE_5XX);
        final Mono<List<PlaceSearchResult.Place>> monoErrorTest = serverErrorClient.getPlaceList(new TestPlaceSearchRequest());

        StepVerifier
                .create(monoErrorTest)
                .expectError(SearchAPIExternalServerException.class)
                .verify();
    }

    @Test
    @DisplayName("External API 응답 오류로 Retry 시도가 모두 실패하면 WebClientRetryExhaustedException 오류를 던짐")
    void getPlaceList_retryError() {
        final SearchAPIClient<TestPlaceSearchRequest> retryClient = new SearchAPIClient<>(getWebClient("test/timeout")) {
            @Override
            protected Mono<TestPlaceSearchResponse> getResponse(final TestPlaceSearchRequest params) {
                return webClient.get()
                        .retrieve()
                        .onStatus(HttpStatusCode::is5xxServerError, convert5xxException())
                        .bodyToMono(TestPlaceSearchResponse.class)
                        .retryWhen(buildRetrySpec());
            }
        };

        mockBackEnd.enqueue(MOCK_RESPONSE_5XX); // Initial Request
        mockBackEnd.enqueue(MOCK_RESPONSE_5XX); // Retry #1
        mockBackEnd.enqueue(MOCK_RESPONSE_5XX); // Retry #2
        mockBackEnd.enqueue(MOCK_RESPONSE_5XX); // Retry #3

        final Mono<List<PlaceSearchResult.Place>> monoErrorTest =
                retryClient.getPlaceList(new TestPlaceSearchRequest());

        StepVerifier
                .create(monoErrorTest)
                .expectErrorMatches(throwable ->
                        throwable instanceof WebClientRetryExhaustedException &&
                        throwable.getMessage().contains(String.format("Retry %s회 실패", RETRY_MAX_ATTEMPTS)))
                .verify();
    }

    public WebClient getWebClient(final String baseUrl) {
        return WebClient.builder().baseUrl(
                mockBackEnd.url(baseUrl).toString()).build();
    }

    @AfterEach
    public void tearDownServer() throws IOException {
        mockBackEnd.shutdown();
    }

    public static class TestPlaceSearchRequest implements PlaceSearchRequest {
    }

    public static class TestPlaceSearchResponse implements PlaceSearchResponse {
        @Override
        public List<PlaceSearchResult.Place> getPlaceList() {
            return List.of(new PlaceSearchResult.Place("MOCKING RESPONSE"));
        }
    }
}