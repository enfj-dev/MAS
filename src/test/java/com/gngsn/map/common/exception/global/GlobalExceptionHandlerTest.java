package com.gngsn.map.common.exception.global;

import com.gngsn.map.common.dto.ErrorResponse;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import jakarta.validation.constraints.NotBlank;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@WebFluxTest(GlobalExceptionHandlerTest.TestController.class)
//@Import({GlobalExceptionHandler.class})
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @InjectMocks
    private TestController testController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(testController)
                .controllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    @Ignore
    void globalException_RuntimeException() {
        webTestClient
                .get()
                .uri("/runtimeException")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> Assertions.assertEquals("서비스에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 보세요.", errorResponse.getMessage()));
    }

    @Test
    @Ignore
    void globalException_ConstraintViolationException() {
        webTestClient
                .get()
                .uri("/constraintViolationException?query")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> Assertions.assertEquals("Bad Request", errorResponse.getMessage()))
                ;
    }

    @Validated
    @RestController
    public static class TestController {

        @GetMapping("/searchAPIBadRequestException")
        public Mono<ResponseEntity<String>> searchAPIBadRequestException() {
            throw new SearchAPIBadRequestException("Mocking Test");
        }

        @GetMapping("/constraintViolationException")
        public ResponseEntity<String> constraintViolationException(@RequestParam @NotBlank final String query) {
//            throw new ConstraintViolationException(Set.of());
            return ResponseEntity.ok("Mocking");
        }
        @GetMapping("/runtimeException")
        public Mono<ResponseEntity<String>> runtimeException() {
            throw new RuntimeException("Mocking Test");
        }
    }
}