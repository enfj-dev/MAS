package com.gngsn.map.common.exception.global;

import com.gngsn.map.common.dto.ErrorResponse;
import com.gngsn.map.search.exception.SearchAPIBadRequestException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@WebFluxTest(GlobalExceptionHandlerTest.TestController.class)
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @InjectMocks
    private TestController testController;
    private WebTestClient webTestClient;
    private ValidatorFactory factory;
    private Validator validator;

    @BeforeEach
    public void setup() {
        webTestClient = WebTestClient.bindToController(testController)
                .controllerAdvice(GlobalExceptionHandler.class)
                .build();

        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void globalException_SearchAPIBadRequestException() {
        webTestClient
                .get()
                .uri("/searchAPIBadRequestException")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .value(errorResponse -> Assertions.assertEquals("서비스에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 보세요.", errorResponse.getMessage()));
    }

    @Test
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
    void globalException_badRequest() {
        webTestClient
                .get()
                .uri("/badRequest")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value(errorResponse -> Assertions.assertTrue(errorResponse.contains("Required query parameter 'query' is not present")))
                ;
    }

    @Test
    void globalException_constraintViolationException() {
        final TestDTO testDTO = new TestDTO("");

        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final ResponseEntity<ErrorResponse> errorResponse = globalExceptionHandler.constraintViolationException(
            new ConstraintViolationException(validator.validate(testDTO))
        );

        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        Assertions.assertNotNull(errorResponse.getBody());
        Assertions.assertEquals("[field] 공백일 수 없습니다.", errorResponse.getBody().getMessage());
    }

    @AfterEach
    public void tearEnd() {
        factory.close();
    }

    @Validated
    @RestController
    public static class TestController {

        @GetMapping("/searchAPIBadRequestException")
        public Mono<ResponseEntity<String>> searchAPIBadRequestException() {
            throw new SearchAPIBadRequestException("Mocking Test");
        }

        @GetMapping("/badRequest")
        public ResponseEntity<String> badRequest(@RequestParam final String query) {
            return ResponseEntity.ok("Mocking");
        }
        @GetMapping("/runtimeException")
        public Mono<ResponseEntity<String>> runtimeException() {
            throw new RuntimeException("Mocking Test");
        }
    }


    static class TestDTO {
        @NotBlank
        private String field;

        public TestDTO(final String field) {
            this.field = field;
        }
    }
}