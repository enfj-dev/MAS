package com.gngsn.map.search.model.kakao;


import com.gngsn.map.common.config.WebClientConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Kakao 장소 검색 API 기반의 장소 검색 요청 테스트")
@ExtendWith(SpringExtension.class)
@Import({KakaoPlaceSearchAPIClient.class, WebClientConfiguration.class})
@ContextConfiguration(classes = {KakaoPlaceSearchAPIClientTest.KakaoTestAPIProperty.class})
@TestPropertySource(locations = {"classpath:/secret.properties"})
class KakaoPlaceSearchAPIClientTest {

    @Autowired
    KakaoTestAPIProperty kakaoTestAPIProperty;

    @Test
    @DisplayName("Kakao API의 '카카오' 검색 결과는 RequestSize(5) 만큼의 리스트를 출력")
    public void givenNormalSetting_thenSuccess() {
        final KakaoPlaceSearchAPIClient kakaoPlaceSearchAPIClient = new KakaoPlaceSearchAPIClient(
                new WebClientConfiguration().webClient(),
                kakaoTestAPIProperty.getApiUri(),
                kakaoTestAPIProperty.getApiSecretKey());

        final Mono<KakaoPlaceSearchResponse> responseMono = kakaoPlaceSearchAPIClient.getResponse(new KakaoPlaceSearchRequest("카카오", 5));

        StepVerifier.create(responseMono)
                .expectNextMatches(kakaoPlaceSearchResponse -> kakaoPlaceSearchResponse.getPlaceList().size() == 5)
                .expectComplete()
                .verify();
    }


    public static class KakaoTestAPIProperty {

        @Value("${kakao.api.uri}")
        private String apiUri;
        @Value("${kakao.api.secret.key}")
        private String apiSecretKey;

        public String getApiUri() {
            return apiUri;
        }

        public String getApiSecretKey() {
            return apiSecretKey;
        }
    }
}
