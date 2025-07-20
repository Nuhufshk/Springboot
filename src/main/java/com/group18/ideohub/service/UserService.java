package com.group18.ideohub.service;

import com.group18.ideohub.dto.JwtDTO;
import com.group18.ideohub.dto.LoginDTO;
import com.group18.ideohub.exception.BadRequestException;
import com.group18.ideohub.exception.ResourceNotFoundException;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.model.profile.ProfileModel;
import com.group18.ideohub.repo.UserRepo;
import com.group18.ideohub.repo.profile.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepo repo;
    private final ProfileRepo profileRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public Users register(LoginDTO user) {
        if (existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        try {
            String uuid = UUID.randomUUID().toString();
            LocalDateTime currentTime = java.time.LocalDateTime.now();

            // create an instance of a new user
            Users DBuser = new Users();

            DBuser.setEmail(user.getEmail());
            DBuser.setPassword(encoder.encode(user.getPassword())); // encrypt the password
            DBuser.setId(uuid);
            DBuser.setCreatedAt(currentTime); // set the creation time

            // create new profile for the created user
            ProfileModel profile = new ProfileModel();

            profile.setId(UUID.randomUUID().toString());
            profile.setUserId(uuid);

            // save the user
            repo.save(DBuser);
            profileRepo.save(profile);

            return DBuser;

        } catch (Exception e) {
            throw new BadRequestException("An internal error occurred");
        }
    }

    public JwtDTO verify(LoginDTO user) {
        if (!existsByEmail(user.getEmail())) {
            throw new ResourceNotFoundException("Email doesn't exist");
        }
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getEmail());
                Users dbUser = repo.findByEmail(user.getEmail());
                return JwtDTO.builder()
                        .jwt(token)
                        .userId(dbUser.getId())
                        .email(dbUser.getEmail())
                        .build();
            } else {
                throw new BadRequestException("Check your password");
            }
        } catch (AuthenticationException e) {
            throw new BadRequestException("Check your password");
        }
    }

    public boolean existsByEmail(String email) {
        return repo.findByEmail(email) != null;
    }

    @Transactional
    public String resetPassword(LoginDTO user) {
        Users dbUser = repo.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new ResourceNotFoundException("Email doesn't exist");
        }

        String hashedPassword = encoder.encode(user.getPassword());

        dbUser.setPassword(hashedPassword);
        dbUser.setUpdatedAt(LocalDateTime.now());

        repo.save(dbUser);

        return "success";
    }

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        String email = authentication.getName();
        Users user = repo.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user.getId();
    }

    public Users getUserById() {
        return repo.findById(getCurrentUser()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public void removeTokenFromCache(String token) {
        // This can be implemented with a caching mechanism like Redis if needed
    }
}
