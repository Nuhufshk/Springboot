package com.group18.ideohub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ideohub_board")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdeohubBoard {
    @Id
    private String id;
    private String title;
    private String description;

}