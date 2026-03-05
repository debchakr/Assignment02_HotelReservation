package com.cgi.hotel.reservation.domain.model.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusRequest {

    private String paymentReference;
}