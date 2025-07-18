package com.group18.ideohub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "post_collection")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostCollection {
    @Id
    private String id;
    private String content;
    private String ideohubBoardId;
    private String userid;

}
