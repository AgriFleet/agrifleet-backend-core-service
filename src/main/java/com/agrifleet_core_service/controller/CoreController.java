package com.agrifleet_core_service.controller;

import com.agrifleet_core_service.entity.BookingEntity;
import com.agrifleet_core_service.entity.VehicleEntity;
import com.agrifleet_core_service.repository.BookingRepository;
import com.agrifleet_core_service.repository.VehicleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // Allows Next.js to access this service
@RestController
@RequestMapping("/api/v1")
public class CoreController {

    private final VehicleRepository vehicleRepository;
    private final BookingRepository bookingRepository;

    public CoreController(VehicleRepository vehicleRepository, BookingRepository bookingRepository) {
        this.vehicleRepository = vehicleRepository;
        this.bookingRepository = bookingRepository;
    }

    // ==========================================
    // VEHICLE ENDPOINTS
    // ==========================================

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
        return ResponseEntity.ok(vehicleRepository.findAll());
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleEntity> getVehicleById(@PathVariable Long id) {
        Optional<VehicleEntity> vehicle = vehicleRepository.findById(id);
        return vehicle.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleEntity> createVehicle(@RequestBody VehicleEntity vehicle) {
        VehicleEntity savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
    }

    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleEntity> updateVehicle(@PathVariable Long id, @RequestBody VehicleEntity updatedData) {
        return vehicleRepository.findById(id).map(existing -> {
            existing.setVehicleType(updatedData.getVehicleType());
            existing.setAvailabilityStatus(updatedData.getAvailabilityStatus());
            existing.setCurrentLat(updatedData.getCurrentLat());
            existing.setCurrentLng(updatedData.getCurrentLng());
            existing.setSpecs(updatedData.getSpecs());
            existing.setPricing(updatedData.getPricing());
            existing.setRating(updatedData.getRating());
            return ResponseEntity.ok(vehicleRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // BOOKING ENDPOINTS
    // ==========================================

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingEntity>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingEntity> getBookingById(@PathVariable Long id) {
        Optional<BookingEntity> booking = bookingRepository.findById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingEntity> createBooking(@RequestBody BookingEntity booking) {
        BookingEntity savedBooking = bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<BookingEntity> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        return bookingRepository.findById(id).map(existing -> {
            existing.setBookingStatus(status);
            return ResponseEntity.ok(bookingRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}