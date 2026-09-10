package com.macthien.ticket_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private boolean active;
    private LocalDateTime createdAt;
}
