package com.cgi.hotel.reservation.listener;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cgi.hotel.reservation.domain.entity.Reservation;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;
import com.cgi.hotel.reservation.repository.ReservationRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankTransferSubscriberTest {

    @Mock
    private ReservationRepository repository;

    @InjectMocks
    private BankTransferSubscriber subscriber;



    @Test
    void testHandleMessage_shouldConfirmReservation_whenPendingPayment() {
        String reservationId = "123";
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.PENDING_PAYMENT);

        when(repository.findById(reservationId)).thenReturn(Optional.of(reservation));

        subscriber.handleMessage("banktransfer " + reservationId);

        verify(repository).save(reservation);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void testHandleMessage_shouldDoNothing_whenMessageIsNull() {
        subscriber.handleMessage(null);
        verifyNoInteractions(repository);
    }


    @Test
    void handleMessage_shouldDoNothing_whenReservationNotFound() {
        String reservationId = "456";
        when(repository.findById(reservationId)).thenReturn(Optional.empty());

        subscriber.handleMessage("banktransfer " + reservationId);

        verify(repository).findById(reservationId);
       verifyNoMoreInteractions(repository);
    }

    @Test
    void handleMessage_shouldDoNothing_whenReservationNotPendingPayment() {
        String reservationId = "789";
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);

        when(repository.findById(reservationId)).thenReturn(Optional.of(reservation));

        subscriber.handleMessage("banktransfer " + reservationId);

        verify(repository).findById(reservationId);
        verify(repository, Mockito.never()).save(reservation);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }
}