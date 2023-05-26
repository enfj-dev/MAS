package com.gngsn.map.search.model.kakao;


import com.gngsn.map.search.config.WebClientConfiguration;
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

@ExtendWith(SpringExtension.class)
@Import({KakaoPlaceSearchAPIClient.class, WebClientConfiguration.class})
@ContextConfiguration(classes = {KakaoPlaceSearchAPIClientTest.KakaoTestAPIProperty.class})
@TestPropertySource(locations = {"classpath:/secret.properties"})
class KakaoPlaceSearchAPIClientTest {

    @Autowired
    KakaoTestAPIProperty kakaoTestAPIProperty;

    @Test
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
