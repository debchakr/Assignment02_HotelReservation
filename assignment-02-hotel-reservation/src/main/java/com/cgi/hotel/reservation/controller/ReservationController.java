package com.cgi.hotel.reservation.controller;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;

import com.cgi.hotel.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cgi.hotel.reservation.domain.api.ReservationRequest;
import com.cgi.hotel.reservation.domain.api.ReservationResponse;

@RestController
@RequestMapping(path = "/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @PostMapping("/confirm")
    public ResponseEntity<ReservationResponse> confirm(
        @RequestBody ReservationRequest request) {

        logger.info("Received reservation request for hotel");

        return ResponseEntity.ok(
            reservationService.confirmReservation(request)
        );
    }
}
