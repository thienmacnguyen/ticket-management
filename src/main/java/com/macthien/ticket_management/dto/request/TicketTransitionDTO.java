package com.macthien.ticket_management.dto.request;

import com.macthien.ticket_management.enums.TicketAction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketTransitionDTO {
    @NotNull(message = "Action không được để trống")
    private TicketAction action;

    @NotNull(message = "Actor ID (Người thao tác) không được để trống")
    private Long actorId;

    @NotNull(message = "Note không được để trống")
    private String note;
}
