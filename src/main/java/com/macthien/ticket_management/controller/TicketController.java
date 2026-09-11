package com.macthien.ticket_management.controller;

import com.macthien.ticket_management.dto.request.*;
import com.macthien.ticket_management.dto.response.*;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import com.macthien.ticket_management.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(@Valid @RequestBody TicketCreateDTO dto) {
        return new ResponseEntity<>(ticketService.createTicket(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDetailResponseDTO> getTicketDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketDetail(id));
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponseDTO>> searchTickets(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long assigneeId,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(ticketService.searchTickets(keyword, status, priority, assigneeId, pageable));
    }

    @PutMapping("/{id}/assignee")
    public ResponseEntity<TicketResponseDTO> assignTicket(@PathVariable Long id, @Valid @RequestBody TicketAssignDTO dto) {
        return ResponseEntity.ok(ticketService.assignTicket(id, dto));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<TicketCommentResponseDTO> addComment(@PathVariable Long id, @Valid @RequestBody CommentCreateDTO dto) {
        return new ResponseEntity<>(ticketService.addComment(id, dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/transitions")
    public ResponseEntity<TicketResponseDTO> transitionStatus(@PathVariable Long id, @Valid @RequestBody TicketTransitionDTO dto) {
        return ResponseEntity.ok(ticketService.transitionStatus(id, dto));
    }
}
