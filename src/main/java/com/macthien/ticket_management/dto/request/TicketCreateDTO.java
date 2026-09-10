package com.macthien.ticket_management.dto.request;

import com.macthien.ticket_management.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateDTO {
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Priority không được để trống")
    private Priority priority;

    @NotNull(message = "reporterId không được để trống")
    private Long reporterId;
}
