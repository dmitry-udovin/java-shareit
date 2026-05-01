package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    private CommentMapper() {

    }

    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getWhoCommented().getName(),
                comment.getDateOfCreate()
        );
    }

}
