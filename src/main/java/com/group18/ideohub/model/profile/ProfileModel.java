package com.group18.ideohub.model.profile;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileModel {
    @Id
    private String id;
    private String userId;
    private String username;

    private String firstName;
    private String middleName;
    private String lastName;

    private String bio;
    private String profilePictureUrl;

}
