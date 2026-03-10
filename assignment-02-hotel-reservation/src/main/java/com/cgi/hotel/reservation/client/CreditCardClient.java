package com.cgi.hotel.reservation.client;

import com.cgi.hotel.reservation.domain.model.creditCard.PaymentStatusRequest;
import com.cgi.hotel.reservation.domain.model.creditCard.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.cgi.hotel.reservation.exception.CreditCardServiceException;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditCardClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${credit-card.base-url}")
    private String baseUrl;

    public boolean verifyPayment(String paymentReference) {

        PaymentStatusRequest request =
            new PaymentStatusRequest(paymentReference);

        try {

            PaymentStatusResponse response =
                webClientBuilder
                    .baseUrl(baseUrl)
                    .build()
                    .post()
                    .uri("/payment-status")
                    .bodyValue(request)
                    .retrieve()

                    //  Error Handling
                    .onStatus(status -> status.is4xxClientError(),
                        clientResponse -> {
                            log.error("4xx error from credit-card service");
                            return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody ->
                                    Mono.error(new RuntimeException(
                                        "Client error from credit-card service: "
                                            + errorBody)));
                        })

                    .onStatus(status -> status.is5xxServerError(),
                        clientResponse -> {
                            log.error("5xx error from credit-card service");
                            return Mono.error(new RuntimeException(
                                "Credit-card service unavailable"));
                        })

                    .bodyToMono(PaymentStatusResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            if (response == null) {
                throw new RuntimeException("Empty response from credit-card service");
            }

            log.info("Credit card payment status received: {}",
                response.getStatus());

            return "CONFIRMED".equalsIgnoreCase(response.getStatus());

        } catch (Exception ex) {

            log.error("Error while verifying credit card payment", ex);
            throw new CreditCardServiceException(
                "Credit-card service unavailable",
                502
            );
        }
    }
}