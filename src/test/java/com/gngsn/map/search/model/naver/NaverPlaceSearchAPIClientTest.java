package com.gngsn.map.search.model.naver;

import com.gngsn.map.common.config.WebClientConfiguration;
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
@ContextConfiguration(classes = NaverPlaceSearchAPIClientTest.NaverTestAPIProperty.class)
@TestPropertySource(locations = "classpath:/secret.properties")
class NaverPlaceSearchAPIClientTest {

    private NaverPlaceSearchAPIClient naverPlaceSearchAPIClient;

    @Autowired
    NaverPlaceSearchAPIClientTest.NaverTestAPIProperty property;


    @BeforeEach
    public void setUp() {
        naverPlaceSearchAPIClient = new NaverPlaceSearchAPIClient(
                new WebClientConfiguration().webClient(),
                property.getApiUri(),
                property.getApiClientId(),
                property.getApiClientSecret());
    }

    @Test
    public void test() throws Exception {
        naverPlaceSearchAPIClient.getResponse(new NaverPlaceSearchRequest("카카오", 5))
                .subscribe(res -> {
                    Assertions.assertEquals(5, res.getPlaceList().size());
                });
        Thread.sleep(2_000);
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