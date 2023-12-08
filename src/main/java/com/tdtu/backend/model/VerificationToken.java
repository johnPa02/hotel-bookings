package com.tdtu.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;
    private LocalDateTime expiryDate;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true,cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
