package com.group18.ideohub.controller.profile;

import com.group18.ideohub.dto.ProfileDTO;
import com.group18.ideohub.dto.ProfileGetDTO;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Get the current user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found") })
    @GetMapping
    public ResponseEntity<RegisterResponse<ProfileDTO>> getProfile() {
        return ResponseEntity.ok(
                new RegisterResponse<>(true, "Profile retrieved successfully", profileService.getProfile()));
    }

    @Operation(summary = "Edit the current user's profile")
    @PostMapping
    public ResponseEntity<RegisterResponse<String>> editProfile(
            @RequestPart("profile") ProfileGetDTO profileGetDTO,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        return ResponseEntity.ok(
                new RegisterResponse<>(true, "Profile updated successfully",
                        profileService.editProfile(profileGetDTO, profilePicture)));
    }
}