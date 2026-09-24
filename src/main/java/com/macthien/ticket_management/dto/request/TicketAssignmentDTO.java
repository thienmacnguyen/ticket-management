package com.macthien.ticket_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketAssignmentDTO {
    @NotNull (message = "New assign ID không được để trống")
    private Long newAssigneeId;

    @NotNull(message = "Actor ID (Người thao tác) không được để trống")
    private Long actorId;

    private String reason;
}
