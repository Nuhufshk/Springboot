package com.group18.ideohub.service.profile;

import com.group18.ideohub.dto.ProfileDTO;
import com.group18.ideohub.dto.ProfileGetDTO;
import com.group18.ideohub.exception.ResourceNotFoundException;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.model.profile.ProfileModel;
import com.group18.ideohub.repo.UserRepo;
import com.group18.ideohub.repo.profile.ProfileRepo;
import com.group18.ideohub.service.UserService;
import com.group18.ideohub.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepo profileRepo;
    private final UserService userService;
    private final UserRepo userRepo;
    private final CloudinaryService cloudinaryservice;

    public ProfileDTO getProfile() {
        String currentUserId = userService.getCurrentUser();
        ProfileModel profileModel = profileRepo.findByUserId(currentUserId);
        if (profileModel == null) {
            throw new ResourceNotFoundException("Profile not found for the current user");
        }

        Users user = userRepo.findById(currentUserId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setUserId(profileModel.getUserId());
        profileDTO.setUsername(profileModel.getUsername());
        profileDTO.setFirstName(profileModel.getFirstName());
        profileDTO.setMiddleName(profileModel.getMiddleName());
        profileDTO.setLastName(profileModel.getLastName());
        profileDTO.setBio(profileModel.getBio());
        profileDTO.setEmail(user.getEmail());
        profileDTO.setProfilePictureUrl(profileModel.getProfilePictureUrl());

        return profileDTO;
    }

    public String editProfile(ProfileGetDTO profileGetDTO, MultipartFile profilePicture) {
        String currentUserId = userService.getCurrentUser();
        ProfileModel profileModel = profileRepo.findByUserId(currentUserId);

        if (profileModel == null) {
            profileModel = new ProfileModel();
            profileModel.setUserId(currentUserId);
        }

        if (profileGetDTO.getUsername() != null && !profileGetDTO.getUsername().isEmpty()) {
            profileModel.setUsername(profileGetDTO.getUsername());
        }
        if (profileGetDTO.getFirstName() != null && !profileGetDTO.getFirstName().isEmpty()) {
            profileModel.setFirstName(profileGetDTO.getFirstName());
        }
        if (profileGetDTO.getMiddleName() != null && !profileGetDTO.getMiddleName().isEmpty()) {
            profileModel.setMiddleName(profileGetDTO.getMiddleName());
        }
        if (profileGetDTO.getLastName() != null && !profileGetDTO.getLastName().isEmpty()) {
            profileModel.setLastName(profileGetDTO.getLastName());
        }
        if (profileGetDTO.getBio() != null && !profileGetDTO.getBio().isEmpty()) {
            profileModel.setBio(profileGetDTO.getBio());
        }

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String pictureUrl = cloudinaryservice.uploadAndReturnUrl(profilePicture);
            profileModel.setProfilePictureUrl(pictureUrl);
        }

        profileRepo.save(profileModel);

        return "Profile updated successfully";
    }
}
