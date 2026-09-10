package com.macthien.ticket_management.dto.response;

import com.macthien.ticket_management.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketStatusHistoryResponseDTO {
    private Long id;
    private TicketStatus fromStatus;
    private TicketStatus toStatus;
    private EmployeeResponseDTO changedBy;
    private String note;
    private LocalDateTime changedAt;
}