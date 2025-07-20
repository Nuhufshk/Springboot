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
public class BoardsDTO {
    private String Title;
    private String description;
    private String layout;
    private boolean isPublic;
}
