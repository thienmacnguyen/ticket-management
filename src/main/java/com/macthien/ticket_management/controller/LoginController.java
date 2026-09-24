package com.macthien.ticket_management.controller;

import com.macthien.ticket_management.dto.request.LoginDTO;
import com.macthien.ticket_management.dto.request.TicketAssignmentDTO;
import com.macthien.ticket_management.dto.response.CurrentUserResponseDTO;
import com.macthien.ticket_management.dto.response.LoginResponseDTO;
import com.macthien.ticket_management.dto.response.TicketAssignmentHistoryResponseDTO;
import com.macthien.ticket_management.repository.EmployeeRepository;
import com.macthien.ticket_management.service.LoginService;
import com.macthien.ticket_management.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final TicketService ticketService;
    private final EmployeeRepository employeeRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(loginService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDTO> getCurrentUser() {
        return ResponseEntity.ok(loginService.getCurrentUser());
    }

    @PutMapping("/{ticketId}/assignee")
    public ResponseEntity<TicketAssignmentHistoryResponseDTO> reassignTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketAssignmentDTO dto) {
        return ResponseEntity.ok(ticketService.reAssignTicket(ticketId, dto));
    }
}
