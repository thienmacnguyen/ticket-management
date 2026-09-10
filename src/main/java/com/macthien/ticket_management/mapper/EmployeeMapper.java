package com.macthien.ticket_management.mapper;

import com.macthien.ticket_management.dto.response.EmployeeResponseDTO;
import com.macthien.ticket_management.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponseDTO toResponseDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}