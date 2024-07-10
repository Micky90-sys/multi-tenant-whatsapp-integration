package com.example.multitenantapp.controller;

import com.example.multitenantapp.entity.Booking;
import com.example.multitenantapp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestParam Long userId, @RequestBody Booking bookingDetails) {
        try {
            Booking booking = bookingService.createBooking(userId, bookingDetails);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
