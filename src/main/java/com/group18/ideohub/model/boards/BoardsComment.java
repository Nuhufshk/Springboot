package com.group18.ideohub.model.boards;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class BoardsComment {
    private String commentId;
    private String boardId;
    private String userCommentId;

    private String commentText;// Text of the comment or caption
    private String imageUrl;
    private String LinkUrl;

    private String createdAt;
    private String updatedAt;
}
