package ru.practicum.shareit.comment.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.comment.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toCommentFromRequestDto(CommentRequestDto commentRequestDto);


    // По аналогии со сгенерированным классом CommentMapperImpl
    default CommentResponseDto toCommentResponseDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();

        if (comment.getAuthor() != null) {
            commentResponseDto.setAuthorName(comment.getAuthor().getName());
        }

        return commentResponseDto;
    }
}
