package com.tdtu.backend.repository;

import com.tdtu.backend.model.Booking;
import com.tdtu.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByUser(User user);
}
