package com.example.multitenantapp.repository;

import com.example.multitenantapp.entity.Booking;
import com.example.multitenantapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUserAndDetails(User user, String details);
}
