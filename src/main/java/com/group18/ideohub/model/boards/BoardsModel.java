package com.group18.ideohub.model.boards;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "boards")
public class BoardsModel {
    @Id
    private String boardId;
    private String UserId;

    private String Title;
    private String description;
    private String imageUrl;
    private String layout;

    private String createdAt;
    private String updatedAt;

    private boolean isPublic;
    private List<BoardsComment> comments; // List of comments on the board
    private ArrayList<String> allowedUsers;

}
