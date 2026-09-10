package com.macthien.ticket_management.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketCommentResponseDTO {
    private Long id;
    private EmployeeResponseDTO author;
    private String content;
    private LocalDateTime createdAt;
}