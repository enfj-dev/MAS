package com.gngsn.map.search.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

import static com.gngsn.map.search.Constants.MAX_COUNT_NO_LIMIT;

/**
 * WebClient Configuration.
 */
@Configuration
public class WebClientConfiguration {
    private final Logger log = LoggerFactory.getLogger(WebClientConfiguration.class);

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(nettyHttpClient(connectionProvider())))
                .filter(logRequest())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    public HttpClient nettyHttpClient(ConnectionProvider connectionProvider) {
        return HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5_000)
                .doOnConnected(connection ->
                        connection
                                .addHandlerLast(new ReadTimeoutHandler(5))
                                .addHandlerLast(new WriteTimeoutHandler(5))
                )
                .responseTimeout(Duration.ofSeconds(6));
    }

    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("mas-web-client-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(Duration.ofSeconds(45))
                .pendingAcquireMaxCount(MAX_COUNT_NO_LIMIT)
                .maxIdleTime(Duration.ofSeconds(60L))
                .maxLifeTime(Duration.ofSeconds(60L))
                .build();
    }
}
