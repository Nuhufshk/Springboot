package com.group18.ideohub.controller;

import com.group18.ideohub.dto.JwtDTO;
import com.group18.ideohub.dto.LoginDTO;
import com.group18.ideohub.model.Users;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user input or email already exists",
                    content = @Content) })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse<Users>> register(@Valid @RequestBody LoginDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        Users registeredUser = service.register(user);
        return ResponseEntity.ok(new RegisterResponse<>(true, "User registered successfully", registeredUser));
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or user does not exist",
                    content = @Content) })
    @PostMapping("/login")
    public ResponseEntity<RegisterResponse<JwtDTO>> login(@Valid @RequestBody LoginDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        JwtDTO validity = service.verify(user);
        return ResponseEntity.ok(new RegisterResponse<>(true, "User authenticated successfully", validity));
    }

    @Operation(summary = "Reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user input or email does not exist",
                    content = @Content) })
    @PostMapping("/reset-password")
    public ResponseEntity<RegisterResponse<String>> resetPassword(@Valid @RequestBody LoginDTO user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new RegisterResponse<>(false, errorMsg, null));
        }
        String resetStatus = service.resetPassword(user);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Password reset successfully", resetStatus));
    }

    @Operation(summary = "Logout a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully"),
            @ApiResponse(responseCode = "400", description = "No token provided") })
    @GetMapping("/logout")
    public ResponseEntity<RegisterResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            service.removeTokenFromCache(token);
            return ResponseEntity.ok(new RegisterResponse<>(true, "User logged out successfully", "Logout successful"));
        }
        return ResponseEntity.badRequest().body(new RegisterResponse<>(false, "No token provided", null));
    }
}
