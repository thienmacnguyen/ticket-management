package com.macthien.ticket_management.service;

import com.macthien.ticket_management.dto.request.EmployeeCreateDTO;
import com.macthien.ticket_management.dto.request.TicketAssignDTO;
import com.macthien.ticket_management.dto.request.TicketTransitionDTO;
import com.macthien.ticket_management.dto.response.EmployeeResponseDTO;
import com.macthien.ticket_management.dto.response.TicketDetailResponseDTO;
import com.macthien.ticket_management.dto.response.TicketResponseDTO;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO dto);
}
