package com.macthien.ticket_management.service;

import com.macthien.ticket_management.dto.request.*;
import com.macthien.ticket_management.dto.response.TicketAssignmentHistoryResponseDTO;
import com.macthien.ticket_management.dto.response.TicketCommentResponseDTO;
import com.macthien.ticket_management.dto.response.TicketDetailResponseDTO;
import com.macthien.ticket_management.dto.response.TicketResponseDTO;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    TicketResponseDTO createTicket(TicketCreateDTO dto);
    TicketDetailResponseDTO getTicketDetail(Long id);
    Page<TicketResponseDTO> searchTickets(String keyword, TicketStatus status, Long assigneeId, Pageable pageable);
    TicketResponseDTO assignTicket(Long id, TicketAssignDTO dto);
    TicketAssignmentHistoryResponseDTO reAssignTicket(Long id, TicketAssignmentDTO dto);
    TicketCommentResponseDTO addComment(Long id, CommentCreateDTO dto);
    TicketResponseDTO transitionStatus(Long id, TicketTransitionDTO dto);
    void validateActor(Long actorIdFromRequest, Long actorIdFromToken, boolean isAdmin);
}
