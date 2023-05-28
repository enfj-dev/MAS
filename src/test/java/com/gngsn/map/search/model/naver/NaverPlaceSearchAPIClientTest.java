package com.gngsn.map.search.model.naver;

import com.gngsn.map.common.config.WebClientConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Kakao 장소 검색 API 기반의 장소 검색 요청 테스트")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NaverPlaceSearchAPIClientTest.NaverTestAPIProperty.class)
@TestPropertySource(locations = "classpath:/secret.properties")
class NaverPlaceSearchAPIClientTest {

    @Autowired
    NaverPlaceSearchAPIClientTest.NaverTestAPIProperty property;

    @Test
    @DisplayName("네이버 API의 '카카오' 검색 결과는 RequestSize(5) 만큼의 리스트를 출력")
    public void test() throws Exception {
        final NaverPlaceSearchAPIClient naverPlaceSearchAPIClient = new NaverPlaceSearchAPIClient(
                new WebClientConfiguration().webClient(),
                property.getApiUri(),
                property.getApiClientId(),
                property.getApiClientSecret());

        final Mono<NaverPlaceSearchResponse> responseMono =
                naverPlaceSearchAPIClient.getResponse(new NaverPlaceSearchRequest("카카오", 5));

        StepVerifier.create(responseMono)
                .expectNextMatches(naverPlaceSearchResponse -> naverPlaceSearchResponse.getPlaceList().size() == 5)
                .expectComplete()
                .verify();
    }

    public static class NaverTestAPIProperty {
        @Value("${naver.api.uri}")
        String apiUri;
        @Value("${naver.api.client.id}")
        String apiClientId;
        @Value("${naver.api.client.secret}")
        String apiClientSecret;

        public String getApiUri() {
            return apiUri;
        }

        public String getApiClientId() {
            return apiClientId;
        }

        public String getApiClientSecret() {
            return apiClientSecret;
        }
    }
}