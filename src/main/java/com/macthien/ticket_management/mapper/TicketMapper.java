package com.macthien.ticket_management.mapper;

import com.macthien.ticket_management.dto.response.*;
import com.macthien.ticket_management.entity.Ticket;
import com.macthien.ticket_management.entity.TicketComment;
import com.macthien.ticket_management.entity.TicketStatusHistory;
import com.macthien.ticket_management.enums.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketMapper {

    private final EmployeeMapper employeeMapper;

    public TicketResponseDTO toResponseDTO(Ticket entity) {
        if (entity == null) return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(entity.getId());
        dto.setTicketCode(entity.getTicketCode());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPriority(entity.getPriority());
        dto.setStatus(entity.getStatus());
        dto.setVersion(entity.getVersion());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setResolvedAt(entity.getResolvedAt());

        dto.setReporter(employeeMapper.toResponseDTO(entity.getReporter()));
        dto.setAssignee(employeeMapper.toResponseDTO(entity.getAssignee()));

        return dto;
    }

    public TicketCommentResponseDTO toCommentResponseDTO(TicketComment entity) {
        if (entity == null) return null;

        TicketCommentResponseDTO dto = new TicketCommentResponseDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());

        dto.setAuthor(employeeMapper.toResponseDTO(entity.getAuthor()));
        return dto;
    }

    public TicketStatusHistoryResponseDTO toHistoryResponseDTO(TicketStatusHistory entity) {
        if (entity == null) return null;

        TicketStatusHistoryResponseDTO dto = new TicketStatusHistoryResponseDTO();
        dto.setId(entity.getId());
        dto.setFromStatus(entity.getFromStatus());
        dto.setToStatus(entity.getToStatus());
        dto.setNote(entity.getNote());
        dto.setChangedAt(entity.getChangedAt());

        dto.setChangedBy(employeeMapper.toResponseDTO(entity.getChangedBy()));
        return dto;
    }

    public List<TicketCommentResponseDTO> toCommentResponseDTOList(List<TicketComment> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toCommentResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TicketStatusHistoryResponseDTO> toHistoryResponseDTOList(List<TicketStatusHistory> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toHistoryResponseDTO)
                .collect(Collectors.toList());
    }
}
