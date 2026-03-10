package com.cgi.hotel.reservation.service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cgi.hotel.reservation.repository.ReservationRepository;
import com.cgi.hotel.reservation.client.CreditCardClient;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;
import com.cgi.hotel.reservation.domain.api.ReservationRequest;
import com.cgi.hotel.reservation.domain.api.ReservationResponse;
import com.cgi.hotel.reservation.domain.entity.Reservation;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private CreditCardClient creditCardClient;

    @Mock
    private Reservation reservation;

    @Spy
    @InjectMocks
    private ReservationService service;


    @Test
    void testconfirmReservation_withCash() {
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.getStartDate()).thenReturn(LocalDate.now());
        when(request.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(request.getPaymentMode()).thenReturn(com.cgi.hotel.reservation.domain.model.PaymentMode.CASH);
        doReturn(reservation).when(service).map(any());
        when(reservation.getReservationId()).thenReturn("res123");
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        ReservationResponse response = service.confirmReservation(request);
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(repository).save(reservation);
    }

    @Test
    void testConfirmReservation_withCreditCard_success() {
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.getStartDate()).thenReturn(LocalDate.now());
        when(request.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(request.getPaymentMode()).thenReturn(com.cgi.hotel.reservation.domain.model.PaymentMode.CREDIT_CARD);
        when(request.getPaymentReference()).thenReturn("ref123");
        when(creditCardClient.verifyPayment("ref123")).thenReturn(true);
        doReturn(reservation).when(service).map(any());
        when(reservation.getReservationId()).thenReturn("res456");
        when(reservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);

        ReservationResponse response = service.confirmReservation(request);
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(repository).save(reservation);
        verify(creditCardClient).verifyPayment("ref123");
    }

    @Test
    void testConfirmReservation_WithCreditCardFailureThrowsException() {
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.getStartDate()).thenReturn(LocalDate.now());
        when(request.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(request.getPaymentMode()).thenReturn(com.cgi.hotel.reservation.domain.model.PaymentMode.CREDIT_CARD);
        when(request.getPaymentReference()).thenReturn("refFail");
        when(creditCardClient.verifyPayment("refFail")).thenReturn(false);
        when(service.map(request)).thenReturn(reservation);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.confirmReservation(request));
        assertThat(exception.getMessage()).isEqualTo("Credit card payment failed");
        verify(creditCardClient).verifyPayment("refFail");
    }

    @Test
    void testConfirmReservation_WithBankTransferSetsStatusPendingPayment() {
        ReservationRequest request = mock(ReservationRequest.class);
        when(request.getStartDate()).thenReturn(LocalDate.now());
        when(request.getEndDate()).thenReturn(LocalDate.now().plusDays(1));
        when(request.getPaymentMode()).thenReturn(com.cgi.hotel.reservation.domain.model.PaymentMode.BANK_TRANSFER);
        when(service.map(request)).thenReturn(reservation);
        when(reservation.getReservationId()).thenReturn("res789");
        when(reservation.getStatus()).thenReturn(ReservationStatus.PENDING_PAYMENT);

        ReservationResponse response = service.confirmReservation(request);
        assertThat(response.getStatus()).isEqualTo(ReservationStatus.PENDING_PAYMENT);
        verify(repository).save(reservation);
    }

    @Test
    void testValidateReservation_exceeds30Days_throwsException() {
        ReservationRequest request = mock(ReservationRequest.class);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(31);
        when(request.getStartDate()).thenReturn(startDate);
        when(request.getEndDate()).thenReturn(endDate);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.validateReservation(request)
        );
        assertThat(exception.getMessage()).isEqualTo("Reservation cannot exceed 30 days");
    }

    @Test
    void testValidateReservation_within30Days_doesNotThrow() {
        ReservationRequest request = mock(ReservationRequest.class);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        when(request.getStartDate()).thenReturn(startDate);
        when(request.getEndDate()).thenReturn(endDate);

        assertDoesNotThrow(() -> service.validateReservation(request));
    }


}