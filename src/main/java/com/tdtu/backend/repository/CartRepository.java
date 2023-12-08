package com.tdtu.backend.repository;

import com.tdtu.backend.model.Cart;
import com.tdtu.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}