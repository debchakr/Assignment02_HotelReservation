package com.klm.ivop.bff.domain.api;

import com.cgi.hotel.reservation.domain.model.PaymentMode;
import com.cgi.hotel.reservation.domain.model.RoomSegment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ReservationRequest {

    @NotNull
    private String customerName;

    @NotNull
    private String roomNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private RoomSegment roomSegment;

    @NotNull
    private PaymentMode paymentMode;

    @Size(max = 50)
    private String paymentReference;
}