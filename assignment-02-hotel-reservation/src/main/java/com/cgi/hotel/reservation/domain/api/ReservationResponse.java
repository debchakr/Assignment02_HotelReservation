package com.klm.ivop.bff.domain.api;

import com.cgi.hotel.reservation.domain.model.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResponse {

    private String reservationId;
    private ReservationStatus status;
}