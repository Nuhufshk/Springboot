package com.group18.ideohub.controller.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.group18.ideohub.dto.ProfileGetDTO;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.profile.ProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<RegisterResponse<?>> getProfile() {
        try {
            return ResponseEntity.ok(
                    new RegisterResponse<>(true, "Profile retrieved successfully", profileService.getProfile()));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new RegisterResponse<>(false,
                            "An error occurred while processing your request. Error :" + e.getMessage(), null));
        }

    }

    @PostMapping
    public ResponseEntity<RegisterResponse<?>> editProfile(
            @RequestPart("profile") ProfileGetDTO profileGetDTO,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        try {
            return ResponseEntity.ok(
                    new RegisterResponse<>(true, "Profile updated successfully",
                            profileService.editProfile(profileGetDTO, profilePicture)));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new RegisterResponse<>(false,
                            "An error occurred while processing your request. Error :" + e.getMessage(), null));
        }
    }

}