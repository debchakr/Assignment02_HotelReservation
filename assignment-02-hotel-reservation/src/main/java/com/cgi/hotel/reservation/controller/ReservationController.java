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
//import com.yourpackage.DomainToApiMapper;
//import com.yourpackage.ApiToDomainMapper;
import com.klm.ivop.bff.domain.api.ReservationRequest;
import com.klm.ivop.bff.domain.api.ReservationResponse;

@RestController
@RequestMapping(path = "/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/confirm")
    public ResponseEntity<ReservationResponse> confirm(
        @RequestBody ReservationRequest request) {

        return ResponseEntity.ok(
            reservationService.confirmReservation(request)
        );
    }
}
