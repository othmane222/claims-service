package com.example.claimconnect_service.service;


import com.example.claimconnect_service.Dto.ClaimDTO;
import com.example.claimconnect_service.Dto.FlightDTO;
import com.example.claimconnect_service.Dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;

@Service
public class ClaimsService {

    @Autowired
    private RedisTemplate<String, ClaimDTO> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private static final String FLIGHT_SERVICE_URL = "http://localhost:8080/api/flights/";
    private static final String RESERVATION_SERVICE_URL = "http://localhost:8081/api/reservations/passenger/";

    // CREATE: Create a new claim
    public ClaimDTO createClaim(ClaimDTO claimDTO) {
        // Fetch flight details
        FlightDTO flightDTO = restTemplate.getForObject(FLIGHT_SERVICE_URL + claimDTO.getFlightId(), FlightDTO.class);
        if (flightDTO == null) {
            throw new IllegalArgumentException("Flight not found with ID: " + claimDTO.getFlightId());
        }

        // Fetch reservation details
        ReservationDTO reservationDTO = restTemplate.getForObject(RESERVATION_SERVICE_URL + claimDTO.getCin(), ReservationDTO.class);
        if (reservationDTO == null) {
            throw new IllegalArgumentException("Reservation not found for passenger with CIN: " + claimDTO.getCin());
        }

        // Set creation date and initial status
        claimDTO.setDate(new Date());
        claimDTO.setStatus("PENDING");

        // Save claim to Redis
        redisTemplate.opsForValue().set(claimDTO.getClaimId(), claimDTO);

        return claimDTO;
    }

    // READ: Get a claim by its ID
    public ClaimDTO getClaimById(String claimId) {
        return redisTemplate.opsForValue().get(claimId);
    }

    // UPDATE: Update an existing claim
    public ClaimDTO updateClaim(String claimId, ClaimDTO claimDTO) {
        ClaimDTO existingClaim = redisTemplate.opsForValue().get(claimId);
        if (existingClaim == null) {
            throw new IllegalArgumentException("Claim not found with ID: " + claimId);
        }

        // Update claim details
        existingClaim.setDescription(claimDTO.getDescription());
        existingClaim.setStatus(claimDTO.getStatus());

        // Save updated claim to Redis
        redisTemplate.opsForValue().set(claimId, existingClaim);

        return existingClaim;
    }

    // DELETE: Delete a claim from Redis
    public void deleteClaim(String claimId) {
        ClaimDTO existingClaim = redisTemplate.opsForValue().get(claimId);
        if (existingClaim == null) {
            throw new IllegalArgumentException("Claim not found with ID: " + claimId);
        }

        // Delete the claim from Redis
        redisTemplate.delete(claimId);
    }
}
