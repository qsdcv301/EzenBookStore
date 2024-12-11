package ezen.team.ezenbookstore.dto;

import lombok.Data;

@Data
public class BookDescriptionDto {
    private Long id;
    private String description;
    private String writer;
    private String contents;
}

