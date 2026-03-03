package com.cgi.hotel.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.klm.ivop.bff.domain.entity.Reservation;
import com.cgi.hotel.reservation.domain.model.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByStatus(ReservationStatus status);
}