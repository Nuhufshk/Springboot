package com.group18.ideohub.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class JwtDTO {
    private String jwt;
    private String userId;
    private String email;
}
