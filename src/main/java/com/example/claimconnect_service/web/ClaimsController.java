package com.example.claimconnect_service.web;


import com.example.claimconnect_service.Dto.ClaimDTO;
import com.example.claimconnect_service.service.ClaimsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "http://localhost:3000")
public class ClaimsController {

    @Autowired
    private ClaimsService claimsService;

    // CREATE: Create a new claim
    @PostMapping
    public ClaimDTO createClaim(@RequestBody ClaimDTO claimDTO) {
        return claimsService.createClaim(claimDTO);
    }

    // READ: Get a claim by ID
    @GetMapping("/{claimId}")
    public ClaimDTO getClaim(@PathVariable String claimId) {
        return claimsService.getClaimById(claimId);
    }

    // UPDATE: Update an existing claim by ID
    @PutMapping("/{claimId}")
    public ClaimDTO updateClaim(@PathVariable String claimId, @RequestBody ClaimDTO claimDTO) {
        return claimsService.updateClaim(claimId, claimDTO);
    }

    // DELETE: Delete a claim by ID
    @DeleteMapping("/{claimId}")
    public void deleteClaim(@PathVariable String claimId) {
        claimsService.deleteClaim(claimId);
    }
}
