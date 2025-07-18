package com.group18.ideohub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Document(collection = "collaborator_collection")
public class CollaboratorCollection {
    @Id
    private String id;

    private String postId;
    private String userId;

}