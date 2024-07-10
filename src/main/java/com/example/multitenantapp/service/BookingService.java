package com.example.multitenantapp.service;

import com.example.multitenantapp.entity.Booking;
import com.example.multitenantapp.entity.User;
import com.example.multitenantapp.exception.ResourceNotFoundException;
import com.example.multitenantapp.repository.BookingRepository;
import com.example.multitenantapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(Long userId, Booking bookingDetails) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check for existing similar booking
        boolean exists = bookingRepository.existsByUserAndDetails(user, bookingDetails.getDetails());
        if (exists) {
            throw new IllegalStateException("Booking already exists for this user with the same details");
        }

        bookingDetails.setUser(user);
        return bookingRepository.save(bookingDetails);
    }
}
