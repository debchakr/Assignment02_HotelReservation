package com.cgi.hotel.reservation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.cgi.hotel.reservation.repository.ReservationRepository;
import com.cgi.hotel.reservation.client.CreditCardClient;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;
import com.cgi.hotel.reservation.domain.api.ReservationRequest;
import com.cgi.hotel.reservation.domain.api.ReservationResponse;
import com.cgi.hotel.reservation.domain.entity.Reservation;
import lombok.AllArgsConstructor;

import java.time.temporal.ChronoUnit;


@Slf4j
@AllArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository repository;
    private final CreditCardClient creditCardClient;



    public ReservationResponse confirmReservation(ReservationRequest request) {

        validateReservation(request);

        Reservation reservation = map(request);

        switch (request.getPaymentMode()) {

            case CASH -> reservation.setStatus(ReservationStatus.CONFIRMED);

            case CREDIT_CARD -> {
                boolean success = creditCardClient.verifyPayment(
                    request.getPaymentReference()
                );
                if (!success) {
                    throw new RuntimeException("Credit card payment failed");
                }
                reservation.setStatus(ReservationStatus.CONFIRMED);
            }

            case BANK_TRANSFER ->
                reservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        }

        repository.save(reservation);

        return new ReservationResponse(
            reservation.getReservationId(),
            reservation.getStatus()
        );
    }

    protected void validateReservation(ReservationRequest request) {
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days > 30) {
            throw new IllegalArgumentException("Reservation cannot exceed 30 days");
        }
    }

    private Reservation map(ReservationRequest request) {

        return Reservation.builder()
            .customerName(request.getCustomerName())
            .roomNumber(request.getRoomNumber())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .roomSegment(request.getRoomSegment())
            .paymentMode(request.getPaymentMode())
            .paymentReference(request.getPaymentReference())
            .status(ReservationStatus.PENDING_PAYMENT) // default
            .build();
    }

}
