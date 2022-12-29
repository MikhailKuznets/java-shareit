package ru.practicum.shareit.comment.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toCommentFromRequestDto(CommentRequestDto commentRequestDto);

    default CommentResponseDto toCommentResponseDto(Comment comment){
        if (Objects.isNull(comment)) {
            return null;
        }
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        commentResponseDto.setCreated(comment.getCreated());
        return commentResponseDto;
    };
}
