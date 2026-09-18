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
    public TicketResponseDTO createTicket(TicketCreateDTO dto);
    public TicketDetailResponseDTO getTicketDetail(Long id);
    public Page<TicketResponseDTO> searchTickets(String keyword, TicketStatus status, Long assigneeId, Pageable pageable);
    public TicketResponseDTO assignTicket(Long id, TicketAssignDTO dto);
    public TicketAssignmentHistoryResponseDTO reAssignTicket(Long id, TicketAssignmentDTO dto);
    public TicketCommentResponseDTO addComment(Long id, CommentCreateDTO dto);
    public TicketResponseDTO transitionStatus(Long id, TicketTransitionDTO dto);
}
