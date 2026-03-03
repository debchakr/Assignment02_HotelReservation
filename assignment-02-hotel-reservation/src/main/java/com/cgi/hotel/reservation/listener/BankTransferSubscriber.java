package com.cgi.hotel.reservation.listener;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cgi.hotel.reservation.repository.ReservationRepository;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;

@Service
@RequiredArgsConstructor
public class BankTransferSubscriber {

    private final ReservationRepository repository;

    public void handleMessage(String message) {

        if (message == null || !message.contains(" ")) {
            return;
        }

        String[] parts = message.split(" ");
        String reservationId = parts[1];

        repository.findById(reservationId)
            .ifPresent(reservation -> {

                if (reservation.getStatus() ==
                    ReservationStatus.PENDING_PAYMENT) {

                    reservation.setStatus(
                        ReservationStatus.CONFIRMED);
                    repository.save(reservation);
                }
            });
    }
}