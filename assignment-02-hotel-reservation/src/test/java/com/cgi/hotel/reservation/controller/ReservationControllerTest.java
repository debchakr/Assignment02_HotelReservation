package com.cgi.hotel.reservation.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.cgi.hotel.reservation.domain.api.ReservationRequest;
import com.cgi.hotel.reservation.domain.api.ReservationResponse;
import com.cgi.hotel.reservation.service.ReservationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;


    @Test
    void testConfirmReservation_Successfully() {
        ReservationRequest request = mock(ReservationRequest.class);
        ReservationResponse expectedResponse = mock(ReservationResponse.class);
        when(reservationService.confirmReservation(any())).thenReturn(expectedResponse);

        ResponseEntity<ReservationResponse> response = reservationController.confirm(request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(reservationService, times(1)).confirmReservation(request);
    }

    @Test
    void testConfirmReservation_handleNullReservationRequest() {
        ReservationRequest request = null;

        try {
            reservationController.confirm(request);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }
}