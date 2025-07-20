package com.group18.ideohub.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardsRespDTO {
    private String boardId;
    private String Title;
    private String description;
    private String layout;
    private boolean isPublic;
    private String imageUrl;
}
