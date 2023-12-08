package com.tdtu.backend.controller;

import com.tdtu.backend.dto.PasswordChangeDTO;
import com.tdtu.backend.model.Booking;
import com.tdtu.backend.model.User;
import com.tdtu.backend.service.BookingService;
import com.tdtu.backend.service.RoomService;
import com.tdtu.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    BookingService bookingService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showDetail(Model model) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        model.addAttribute("user",user);
        return "user-info";
    }
    @GetMapping("/change-password")
    public String showChangePasswordPage(Model model) {
        model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
        return "change-pass";
    }

    @PostMapping("/change-password")
    public String changeUserPassword(@ModelAttribute PasswordChangeDTO passwordChangeDTO, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            Long userId = userOptional.get().getId();
            boolean isPasswordChanged = userService.changePassword(userId, passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword(), passwordChangeDTO.getConfirmPassword());

            if (isPasswordChanged) {
                model.addAttribute("successMessage", "Password changed successfully!");
                return "redirect:/";            } else {
                model.addAttribute("errorMessage", "Không khớp mật khẩu");
                return "change-pass";
            }
        } else {
            model.addAttribute("errorMessage", "User not found.");
            return "change-pass";
        }
    }
    @GetMapping("/history")
    public String historyBooking(Model model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        List<Booking> bookingList = bookingService.findBookingsByUser(user);
        model.addAttribute("bookingList", bookingList);
        return "history-booking";
    }
   @GetMapping("/bookings/details/{id}")
    public String bookingDetails(Model model, @PathVariable Long id){
       Booking booking = bookingService.findBookingById(id)
               .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + id));
       model.addAttribute("booking",booking);
       return "booking-details";
   }
}
