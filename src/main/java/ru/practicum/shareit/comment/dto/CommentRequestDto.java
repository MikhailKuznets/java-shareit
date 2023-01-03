package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Valid
public class CommentRequestDto {
    @NotBlank
    private String text;
}
