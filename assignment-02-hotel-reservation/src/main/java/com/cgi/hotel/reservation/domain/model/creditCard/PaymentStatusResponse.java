package com.cgi.hotel.reservation.domain.model.creditCard;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PaymentStatusResponse {

    private OffsetDateTime lastUpdateDate;
    private String status;
}