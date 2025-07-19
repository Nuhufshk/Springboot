package com.group18.ideohub.repo.profile;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.group18.ideohub.model.profile.ProfileModel;

public interface ProfileRepo extends MongoRepository<ProfileModel, String> {

    ProfileModel findByUserId(String currentUser);

}
