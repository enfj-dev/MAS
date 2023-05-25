package com.gngsn.map.search.model.kakao;

import com.gngsn.map.search.config.WebClientConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KakaoPlaceSearchAPIClientTest.KakaoTestAPIProperty.class)
@TestPropertySource(locations = "classpath:/secret.properties")
class KakaoPlaceSearchAPIClientTest {

    private KakaoPlaceSearchAPIClient kakaoPlaceSearchAPIClient;

    @Autowired
    KakaoTestAPIProperty kakaoTestAPIProperty;


    @BeforeEach
    public void setUp() {
        kakaoPlaceSearchAPIClient = new KakaoPlaceSearchAPIClient(
                new WebClientConfiguration().webClient(), kakaoTestAPIProperty.getApiSecretKey());
    }

    @Test
    public void test() throws Exception {
        kakaoPlaceSearchAPIClient.getResponse(new KakaoPlaceSearchRequest("카카오", 5))
                .subscribe(res -> {
                    Assertions.assertEquals(5, res.getPlaceList().size());
                });
        Thread.sleep(2_000);
    }

    public static class KakaoTestAPIProperty {

        @Value("${kakao.api.secret.key}")
        private String apiSecretKey;

        public String getApiSecretKey() {
            return apiSecretKey;
        }
    }
}