package com.macthien.ticket_management.service.Impl;

import com.macthien.ticket_management.dto.request.*;
import com.macthien.ticket_management.dto.response.*;
import com.macthien.ticket_management.entity.*;
import com.macthien.ticket_management.enums.ErrorCode;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketAction;
import com.macthien.ticket_management.enums.TicketStatus;
import com.macthien.ticket_management.exception.AppException;
import com.macthien.ticket_management.mapper.TicketMapper;
import com.macthien.ticket_management.repository.*;
import com.macthien.ticket_management.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;
    private final TicketCommentRepository commentRepository;
    private final TicketStatusHistoryRepository historyRepository;
    private final TicketMapper ticketMapper;

    private final TicketAssignmentHistoryRepository ticketAssignmentHistoryRepository;
    @Override
    public TicketResponseDTO createTicket(TicketCreateDTO dto) {
        Employee reporter = employeeRepository
                .findById(dto.getReporterId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        if (!reporter.isActive()) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }

        Ticket ticket = new Ticket();
        // TODO [MENTOR REVIEW]: LocalDateTime.toString() tạo mã chứa ':', dấu chấm và phần nano giây.
        // Hãy chọn format ổn định, dễ đọc, đúng độ dài; vẫn phải dựa vào UNIQUE constraint để chống trùng.
        ticket.setTicketCode("TK-" + LocalDateTime.now() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setReporter(reporter);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketMapper.toResponseDTO(ticketRepository.save(ticket));
    }

    @Override
    public TicketDetailResponseDTO getTicketDetail(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));

        List<TicketComment> comments = commentRepository.findByTicketIdOrderByCreatedAtDesc(id);
        List<TicketStatusHistory> histories = historyRepository.findByTicketIdOrderByChangedAtDesc(id);
        TicketDetailResponseDTO response = new TicketDetailResponseDTO();
        TicketResponseDTO baseDTO = ticketMapper.toResponseDTO(ticket);
        response.setId(baseDTO.getId());
        response.setTicketCode(baseDTO.getTicketCode());
        response.setTitle(baseDTO.getTitle());
        response.setDescription(baseDTO.getDescription());
        response.setPriority(baseDTO.getPriority());
        response.setStatus(baseDTO.getStatus());
        response.setReporter(baseDTO.getReporter());
        response.setAssignee(baseDTO.getAssignee());
        response.setVersion(baseDTO.getVersion());
        response.setCreatedAt(baseDTO.getCreatedAt());
        response.setUpdatedAt(baseDTO.getUpdatedAt());
        response.setResolvedAt(baseDTO.getResolvedAt());

        response.setComments(ticketMapper.toCommentResponseDTOList(comments));
        response.setHistories(ticketMapper.toHistoryResponseDTOList(histories));

        return response;
    }

    @Override
    public Page<TicketResponseDTO> searchTickets(String keyword, TicketStatus status, Long assigneeId, Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        Page<Ticket> tickets = ticketRepository.searchTickets(keyword, status, assigneeId, pageable);
        return tickets.map(ticketMapper::toResponseDTO);
    }


    @Override
    public TicketResponseDTO assignTicket(Long id, TicketAssignDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new AppException(ErrorCode.INVALID_ASSIGNMENT);
        }
        Employee assignee = employeeRepository.findById(dto.getAssigneeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        if(!assignee.isActive()) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }
        ticket.setAssignee(assignee);
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketMapper.toResponseDTO(ticketRepository.save(ticket));
    }

    @Transactional
    @Override
    public TicketAssignmentHistoryResponseDTO reAssignTicket(Long id, TicketAssignmentDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        Employee actor = employeeRepository.findById(dto.getActorId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        Employee reAssignee = employeeRepository.findById(dto.getReAssignId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        Employee oldAssignee = ticket.getAssignee();
        String reason = dto.getReason() != null ? dto.getReason().trim() : "";
        if(!reAssignee.isActive()) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }
        if(ticket.getStatus() == TicketStatus.CLOSED) {
            throw new AppException(ErrorCode.INVALID_ASSIGNMENT);
        }
        if (oldAssignee != null && oldAssignee.getId().equals(dto.getReAssignId())) {
            throw new AppException(ErrorCode.INVALID_ASSIGNMENT);
        }
        if (oldAssignee != null && reason == null || reason.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REASON);
        }

        ticket.setAssignee(reAssignee);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);

        TicketAssignmentHistory history = new TicketAssignmentHistory();
        history.setTicket(ticket);
        history.setNewAssignee(reAssignee);
        history.setOldAssignee(oldAssignee);
        history.setChangedBy(actor);
        history.setChangedAt(LocalDateTime.now());
        history.setReason(reason);
        ticketAssignmentHistoryRepository.save(history);
        return ticketMapper.toAssignmentHistoryResponseDTO(history);
    }

    @Override
    public TicketCommentResponseDTO addComment(Long id, CommentCreateDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new AppException(ErrorCode.COMMENT_NOT_ALLOWED);
        }

        Employee author = employeeRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        if(!author.isActive()) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }
        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setContent(dto.getContent());
        return ticketMapper.toCommentResponseDTO(commentRepository.save(comment));
    }

//    @Override
//    @Transactional
//    public TicketResponseDTO transitionStatus(Long id, TicketTransitionDTO dto) {
//        Ticket ticket = ticketRepository.findById(id)
//                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
//
//        Employee actor = employeeRepository.findById(dto.getActorId())
//                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
//        if(!actor.isActive()) {
//            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
//        }
//        TicketStatus fromStatus = ticket.getStatus();
//        TicketStatus toStatus;
//        switch (dto.getAction()) {
//            case START:
//                if (fromStatus != TicketStatus.OPEN) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
//                if (ticket.getAssignee() == null) throw new AppException(ErrorCode.ASSIGNEE_REQUIRED);
//                if (!ticket.getAssignee().getId().equals(actor.getId())) throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
//                toStatus = TicketStatus.IN_PROGRESS;
//                break;
//            case RESOLVE:
//                if (fromStatus != TicketStatus.IN_PROGRESS) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
//                if (dto.getNote() == null || dto.getNote().trim().isEmpty()) throw new AppException(ErrorCode.INVALID_NOTE);
//                toStatus = TicketStatus.RESOLVED;
//                ticket.setResolvedAt(LocalDateTime.now());
//                break;
//            case CLOSE:
//                if (fromStatus != TicketStatus.RESOLVED) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
//                if (!ticket.getReporter().getId().equals(actor.getId())) throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
//                toStatus = TicketStatus.CLOSED;
//                break;
//            case REOPEN:
//                if (fromStatus != TicketStatus.RESOLVED) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
//                if (dto.getNote() == null || dto.getNote().trim().isEmpty()) throw new AppException(ErrorCode.INVALID_NOTE);
//                toStatus = TicketStatus.IN_PROGRESS;
//                ticket.setResolvedAt(null);
//                break;
//            default:
//                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
//
//        }
//        ticket.setStatus(toStatus);
//        ticket.setUpdatedAt(LocalDateTime.now());
//        ticketRepository.save(ticket);
//
//        TicketStatusHistory history = new TicketStatusHistory();
//        history.setTicket(ticket);
//        history.setFromStatus(fromStatus);
//        history.setToStatus(toStatus);
//        history.setChangedBy(actor);
//        history.setNote(dto.getNote());
//        history.setChangedAt(LocalDateTime.now());
//        historyRepository.save(history);
//        return ticketMapper.toResponseDTO(ticket);
//        }
//}

    @Override
    @Transactional
    public TicketResponseDTO transitionStatus(Long id, TicketTransitionDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        Employee actor = employeeRepository.findById(dto.getActorId())
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNEE_REQUIRED));
        Employee assignee = ticket.getAssignee();
        Employee reporter = ticket.getReporter();
        TicketStatus fromStatus = ticket.getStatus();
        boolean hasAssignee = assignee != null;
        boolean actorIsAssignee = hasAssignee && assignee.getId().equals(actor.getId());
        boolean actorIsReporter = reporter.getId().equals(actor.getId());
        TicketStatus toStatus = determineNextStatus(fromStatus, dto.getAction(), hasAssignee, actorIsAssignee, actorIsReporter, dto.getNote());
        ticket.setStatus(toStatus);
        ticketRepository.save(ticket);
        TicketStatusHistory history = new TicketStatusHistory();
        history.setTicket(ticket);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedBy(actor);
        history.setChangedAt(LocalDateTime.now());
        history.setNote(dto.getNote());
        historyRepository.save(history);
        return ticketMapper.toResponseDTO(ticket);

    }

    private TicketStatus determineNextStatus(
            TicketStatus currentStatus,
            TicketAction action,
            boolean hasAssignee,
            boolean actorIsAssignee,
            boolean actorIsReporter,
            String note) {
        if(currentStatus == TicketStatus.OPEN && action == TicketAction.START) {
            if (!hasAssignee || !actorIsAssignee) {
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            return TicketStatus.IN_PROGRESS;
        }

        if(currentStatus == TicketStatus.IN_PROGRESS && action == TicketAction.RESOLVE) {
            if (note == null || note.trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            return TicketStatus.RESOLVED;
        }

        if(currentStatus == TicketStatus.RESOLVED && action == TicketAction.CLOSE) {
            if (!actorIsReporter) {
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            return TicketStatus.CLOSED;
        }

        if(currentStatus == TicketStatus.RESOLVED && action == TicketAction.REOPEN) {
            if (note == null || note.trim().isEmpty()) {
                throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
            }
            return  TicketStatus.IN_PROGRESS;
        }
        throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
    }

    public String validateAndNormalizeAssignment(
            TicketStatus status,
            Long oldAssigneeId,
            Long newAssigneeId,
            boolean newAssigneeActive,
            String reason
    ) {
        if (status == TicketStatus.CLOSED) {
            throw new AppException(ErrorCode.INVALID_ASSIGNMENT);
        }
        if (newAssigneeId == null) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }
        if (!newAssigneeActive) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }
        if (newAssigneeId == oldAssigneeId) {
            throw new AppException(ErrorCode.DUPLICATE_EMPLOYEE);
        }
        reason = reason.trim();
        if (oldAssigneeId == null || reason == null || reason.trim().isEmpty()) {
            return null;
        } else {
            return reason;
        }
    }
}