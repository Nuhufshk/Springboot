package com.group18.ideohub.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ProfileDTO {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
    private String email;
    private String userId;

}
