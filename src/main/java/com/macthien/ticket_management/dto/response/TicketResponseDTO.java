package com.macthien.ticket_management.dto.response;

import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketResponseDTO {
    private Long id;
    private String ticketCode;
    private String title;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private EmployeeResponseDTO reporter;
    private EmployeeResponseDTO assignee;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
}
