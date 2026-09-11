package com.macthien.ticket_management.service.Impl;

import com.macthien.ticket_management.dto.request.TicketAssignDTO;
import com.macthien.ticket_management.dto.request.TicketCreateDTO;
import com.macthien.ticket_management.dto.request.TicketTransitionDTO;
import com.macthien.ticket_management.dto.response.TicketDetailResponseDTO;
import com.macthien.ticket_management.dto.response.TicketResponseDTO;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import com.macthien.ticket_management.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class TicketServiceImpl implements TicketService {
    @Override
    public TicketResponseDTO createTicket(TicketCreateDTO dto) {
        return null;
    }

    @Override
    public TicketDetailResponseDTO getTicketDetail(Long id) {
        return null;
    }

    @Override
    public Page<TicketResponseDTO> searchTickets(String keyword, TicketStatus status, Priority priority, Long assigneeId, Pageable pageable) {
        return null;
    }

    @Override
    public TicketResponseDTO assignTicket(Long id, TicketAssignDTO dto) {
        return null;
    }

    @Override
    public TicketResponseDTO transitionStatus(Long id, TicketTransitionDTO dto) {
        return null;
    }
}
