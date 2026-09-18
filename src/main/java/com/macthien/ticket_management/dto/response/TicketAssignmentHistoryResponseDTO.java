package com.macthien.ticket_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketAssignmentHistoryResponseDTO {
    private Long ticketId;
    private Long oldAssigneeId;
    private Long newAssigneeId;
    private Long changedBy;
    private String reason;
    private LocalDateTime changedAt;
}
