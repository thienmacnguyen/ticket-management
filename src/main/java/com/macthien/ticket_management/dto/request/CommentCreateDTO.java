package com.macthien.ticket_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {

    @NotNull(message = "Author ID không được để trống")
    private Long authorId;

    @NotBlank(message = "Nội dung comment không được để trống")
    private String content;
}
