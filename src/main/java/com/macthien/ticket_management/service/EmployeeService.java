package com.macthien.ticket_management.service;

import com.macthien.ticket_management.dto.request.EmployeeCreateDTO;
import com.macthien.ticket_management.dto.response.EmployeeResponseDTO;

public interface EmployeeService {
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO dto);
}
