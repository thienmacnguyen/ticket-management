package com.macthien.ticket_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
}
