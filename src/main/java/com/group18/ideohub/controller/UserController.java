package com.group18.ideohub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group18.ideohub.dto.JwtDTO;
import com.group18.ideohub.dto.LoginDTO;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse<Users>> register(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {

        // Validate the user input
          if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        
        // Check if phone number already exists
        if (service.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Phone number already exists", null));
        }

        // Register the user
        Users registeredUser = service.register(user);

        if(registeredUser == null) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "An internal error occured", null));

        }
        return ResponseEntity.ok(new RegisterResponse<>(true, "User registered successfully", registeredUser));
    }



    @PostMapping("/login")
    public ResponseEntity<RegisterResponse<JwtDTO>> login(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {
        
        // Validate the user input
          if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        
        // Check if phone number already exists
        if (!service.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Phone number doesn't exists", null));
        }

        if(service.verify(user) == null || "fail".equals(service.verify(user))) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Check your password",  new JwtDTO(service.verify(user))) );
        }

        String validity = service.verify(user);
        return ResponseEntity.ok(new RegisterResponse<>(true, "User authenticated successfully", new JwtDTO(validity)));//returns a jwt
    }


    @PostMapping("/reset-password")
    public ResponseEntity<RegisterResponse<String>> resetPassword(@Valid @RequestBody LoginDTO user,
            BindingResult bindingResult) {
        
        // Validate the user input
          if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        
        // Check if phone number exists
        if (!service.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Phone number doesn't exists", null));
        }

        String resetStatus = service.resetPassword(user);
        if ("fail".equals(resetStatus)) {
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "Failed to reset password", null));
        }

        return ResponseEntity.ok(new RegisterResponse<>(true, "Password reset successfully", resetStatus));
    }

    @GetMapping("/logout")
    public ResponseEntity<RegisterResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // remove thr token from the cache
            service.removeTokenFromCache(token);

            return ResponseEntity.ok(new RegisterResponse<>(true, "User logged out successfully", "Logout successful"));
        }

        return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "No token provided", null));
    }


}
