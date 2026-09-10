package com.macthien.ticket_management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketAssignDTO {

    @NotNull(message = "Assignee ID không được để trống")
    private Long assigneeId;
}
