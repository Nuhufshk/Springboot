package com.group18.ideohub.service.profile;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.group18.ideohub.dto.ProfileDTO;
import com.group18.ideohub.dto.ProfileGetDTO;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.model.profile.ProfileModel;
import com.group18.ideohub.repo.UserRepo;
import com.group18.ideohub.repo.profile.ProfileRepo;
import com.group18.ideohub.service.UserService;

@Service
public class ProfileService {

    private ProfileRepo profileRepo;

    private UserService userService;

    private UserRepo userRepo;

    public ProfileDTO getProfile() {
        ProfileModel profileModel = profileRepo.findByUserId(userService.getCurrentUser());

        Users user = userRepo.findByUserId(userService.getCurrentUser());

        if (profileModel != null) {
            ProfileDTO profileDTO = new ProfileDTO();

            profileDTO.setUserId(profileModel.getUserId());
            profileDTO.setUsername(profileModel.getUsername());
            profileDTO.setFirstName(profileModel.getFirstName());
            profileDTO.setMiddleName(profileModel.getMiddleName());
            profileDTO.setLastName(profileModel.getLastName());
            profileDTO.setBio(profileModel.getBio());
            profileDTO.setEmail(user.getEmail() != null ? user.getEmail() : "");
            profileDTO.setProfilePictureUrl(profileModel.getProfilePictureUrl());

            return profileDTO;
        }
        return null;
    }

    public String editProfile(ProfileGetDTO profileGetDTO, MultipartFile profilePicture) {
        ProfileModel profileModel = profileRepo.findByUserId(userService.getCurrentUser());

        if (profileModel == null) {
            profileModel = new ProfileModel();
            profileModel.setUserId(userService.getCurrentUser());
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

        // Handle profile picture upload logic here
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String pictureUrl = "my profile picture url";
            profileModel.setProfilePictureUrl(pictureUrl);
        }

        profileRepo.save(profileModel);

        return "Profile updated successfully";
    }

}
