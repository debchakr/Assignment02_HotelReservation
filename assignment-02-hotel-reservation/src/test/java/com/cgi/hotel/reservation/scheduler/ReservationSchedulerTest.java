package com.cgi.hotel.reservation.scheduler;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cgi.hotel.reservation.domain.entity.Reservation;
import com.cgi.hotel.reservation.domain.model.PaymentMode;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;
import com.cgi.hotel.reservation.repository.ReservationRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationSchedulerTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private ReservationScheduler scheduler;


    @Test
    void testCancelUnpaidReservations_CancelsBankTransferIfPastDue() {
        Reservation reservation = mock(Reservation.class);
        when(reservation.getPaymentMode()).thenReturn(PaymentMode.BANK_TRANSFER);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 3, 10));
        List<Reservation> pending = Collections.singletonList(reservation);
        when(repository.findByStatus(ReservationStatus.PENDING_PAYMENT)).thenReturn(pending);

        scheduler.cancelUnpaidReservations();

        verify(reservation).setStatus(ReservationStatus.CANCELLED);
        verify(repository).save(reservation);
    }

    @Test
    void testCancelUnpaidReservations_DoesNotCancelIfNotBankTransfer() {
        Reservation reservation = mock(Reservation.class);
        when(reservation.getPaymentMode()).thenReturn(PaymentMode.CASH);
        List<Reservation> pending = Collections.singletonList(reservation);
        when(repository.findByStatus(ReservationStatus.PENDING_PAYMENT)).thenReturn(pending);

        scheduler.cancelUnpaidReservations();

        verify(reservation, never()).setStatus(ReservationStatus.CANCELLED);
        verify(repository, never()).save(reservation);
    }

    @Test
    void cancelUnpaidReservations_DoesNotCancelIfNotPastDue() {
        Reservation reservation = mock(Reservation.class);
        when(reservation.getPaymentMode()).thenReturn(PaymentMode.BANK_TRANSFER);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 3, 20));
        List<Reservation> pending = Collections.singletonList(reservation);
        when(repository.findByStatus(ReservationStatus.PENDING_PAYMENT)).thenReturn(pending);

        scheduler.cancelUnpaidReservations();

        verify(reservation, never()).setStatus(ReservationStatus.CANCELLED);
        verify(repository, never()).save(reservation);
    }

    @Test
    void cancelUnpaidReservations_HandlesEmptyPendingList() {
        when(repository.findByStatus(ReservationStatus.PENDING_PAYMENT)).thenReturn(Collections.emptyList());
        scheduler.cancelUnpaidReservations();
    }
}