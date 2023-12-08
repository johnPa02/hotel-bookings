package com.tdtu.backend.service;

import com.tdtu.backend.dto.UserAdminDto;
import com.tdtu.backend.dto.UserDTO;
import com.tdtu.backend.model.Role;
import com.tdtu.backend.model.User;
import com.tdtu.backend.model.VerificationToken;
import com.tdtu.backend.repository.UserRepository;
import com.tdtu.backend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User user) throws Exception {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new Exception("Email already exists");
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String tokenString = UUID.randomUUID().toString();
        VerificationToken token = new VerificationToken();
        token.setUser(user);
        token.setToken(tokenString);
        token.setExpiryDate(LocalDateTime.now().plusHours(24));
        tokenRepository.save(token);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/confirm-account?token=" + tokenString);

        mailSender.send(mailMessage);
    }
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        if (user != null && user.getId() != null && userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("User cannot be updated because it doesn't exist.");
    }

    @Override
    public void deleteUser(Long userId) {
        List<VerificationToken> tokens = tokenRepository.findByUserId(userId);

        for (VerificationToken token : tokens) {
            tokenRepository.delete(token);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public User registerNewUserAccount(UserDTO accountDto) {
        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        return userRepository.save(user);
    }
    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return false; // New passwords do not match
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                return false; // Old password doesn't match
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
    @Override
    public void createAdminUser(UserAdminDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setActive(true);
        Set<String> roleStrings = userDto.getRoles();
        Set<Role> roles = roleStrings.stream()
                .map(String::toUpperCase)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        userRepository.save(user);
    }

}
