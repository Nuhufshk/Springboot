package com.group18.ideohub.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileGetDTO {
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String bio;
}
