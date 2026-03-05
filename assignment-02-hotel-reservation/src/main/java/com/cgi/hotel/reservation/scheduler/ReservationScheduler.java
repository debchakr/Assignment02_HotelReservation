package com.cgi.hotel.reservation.scheduler;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

import com.cgi.hotel.reservation.domain.entity.Reservation;
import com.cgi.hotel.reservation.repository.ReservationRepository;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;
import com.cgi.hotel.reservation.domain.model.PaymentMode;

@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository repository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelUnpaidReservations() {

        List<Reservation> pending =
            repository.findByStatus(
                ReservationStatus.PENDING_PAYMENT);

        LocalDate today = LocalDate.now();

        for (Reservation reservation : pending) {

            if (reservation.getPaymentMode() ==
                PaymentMode.BANK_TRANSFER &&
                today.isAfter(
                    reservation.getStartDate().minusDays(2))) {

                reservation.setStatus(
                    ReservationStatus.CANCELLED);
                repository.save(reservation);
            }
        }
    }
}