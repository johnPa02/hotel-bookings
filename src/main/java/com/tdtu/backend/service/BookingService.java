package com.tdtu.backend.service;

import com.tdtu.backend.model.Cart;
import com.tdtu.backend.model.Booking;
import com.tdtu.backend.model.User;
import com.tdtu.backend.repository.BookingRepository;
import com.tdtu.backend.repository.CartRepository;
import com.tdtu.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user with ID: " + userId));


        Booking booking = new Booking();
        booking.setUser(user);
        booking.setItems(new ArrayList<>(cart.getItems()));
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalPrice(cart.getTotalPrice());

        bookingRepository.save(booking);

        clearCart(cart);

        return booking;
    }


    private void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public List<Booking> findAllBookings(){
        return bookingRepository.findAll();
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findBookingsByUser(User user){
        return bookingRepository.findBookingsByUser(user);
    }
}

