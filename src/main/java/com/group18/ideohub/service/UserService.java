package com.group18.ideohub.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group18.ideohub.dto.LoginDTO;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public Users register(LoginDTO user) {

        try {
            // generate an id for each user
            String uuid = UUID.randomUUID().toString();
            LocalDateTime currentTime = java.time.LocalDateTime.now();

            // create an instance of a new user
            Users DBuser = new Users();

            DBuser.setEmail(user.getEmail());
            DBuser.setPassword(encoder.encode(user.getPassword())); // encrypt the password
            DBuser.setId(uuid);
            DBuser.setCreatedAt(currentTime); // set the creation time

            // save the user
            repo.save(DBuser);

            // return the user after all is saved
            return DBuser;

        } catch (Exception e) {
            return null;
        }

    }

    public String verify(LoginDTO user) {
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getEmail());
            } else {
                return "fail";
            }

        } catch (AuthenticationException e) {
            return null; // Error occurred while checking user
        }
    }

    public boolean existsByEmail(String email) {
        Users user = repo.findByEmail(email);
        return user != null;
    }

    @Transactional
    public String resetPassword(LoginDTO user) {

        try {
            // Find the user by phone number
            Users dbUser = repo.findByEmail(user.getEmail());
            if (dbUser == null) {
                return "fail";
            }

            String hashedPassword = encoder.encode(user.getPassword());

            dbUser.setPassword(hashedPassword);
            dbUser.setUpdatedAt(LocalDateTime.now());

            repo.save(dbUser);

            return "success";

        } catch (Exception e) {
            return "fail";
        }

    }

    public String getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            // get the user by phone number
            Users user = repo.findByEmail(userId);

            if (user == null) {
                return null;
            }

            // return the user id for all searches
            return user.getId();

        } catch (Exception e) {
            return null; // If an error occurs, return null
        }

    }

    // finds the user id to be used in the chat service
    public Users getUserById() {
        Users user = repo.findById(getCurrentUser()).orElse(null);
        if (user == null) {
            return null;
        }
        return user;
    }

    public void removeTokenFromCache(String token) {
    }
}
