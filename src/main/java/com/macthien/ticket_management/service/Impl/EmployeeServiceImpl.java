package com.macthien.ticket_management.service.Impl;

import com.macthien.ticket_management.dto.request.EmployeeCreateDTO;
import com.macthien.ticket_management.dto.response.EmployeeResponseDTO;
import com.macthien.ticket_management.entity.Employee;
import com.macthien.ticket_management.enums.ErrorCode;
import com.macthien.ticket_management.exception.AppException;
import com.macthien.ticket_management.mapper.EmployeeMapper;
import com.macthien.ticket_management.repository.EmployeeRepository;
import com.macthien.ticket_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl  implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO dto) {
        if (employeeRepository.existsByUsername(dto.getUsername())) {
            throw new AppException(ErrorCode.DUPLICATE_USERNAME);
        }
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }
        Employee employee = new Employee();
        employee.setUsername(dto.getUsername());
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setActive(true);
        employee.setCreatedAt(LocalDateTime.now());
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(saved);
    }
}
