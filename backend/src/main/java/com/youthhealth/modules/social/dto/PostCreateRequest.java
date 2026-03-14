package com.youthhealth.modules.social.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostCreateRequest {
    @NotBlank
    private String content;

    private List<String> images;
}
