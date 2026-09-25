package com.macthien.ticket_management.service.Impl;

import com.macthien.ticket_management.dto.request.LoginDTO;
import com.macthien.ticket_management.dto.response.CurrentUserResponseDTO;
import com.macthien.ticket_management.dto.response.LoginResponseDTO;
import com.macthien.ticket_management.entity.Employee;
import com.macthien.ticket_management.enums.ErrorCode;
import com.macthien.ticket_management.exception.AppException;
import com.macthien.ticket_management.repository.EmployeeRepository;
import com.macthien.ticket_management.security.JwtTokenProvider;
import com.macthien.ticket_management.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginDTO dto) {
        Employee employee = employeeRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        if (!employee.isActive()) {
            throw new AppException(ErrorCode.EMPLOYEE_INACTIVE);
        }

        if(!passwordEncoder.matches(dto.getPassword(), employee.getPasswordHash())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = tokenProvider.generateToken(employee.getId(), employee.getUsername(), employee.getRole());
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(token);
        response.setExpiresAt(LocalDateTime.now().plusDays(1).withNano(0));
        return response;
    }

    @Override
    public CurrentUserResponseDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Long)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        Long currentEmployeeId = (Long) principal;
        Employee employee = employeeRepository.findById(currentEmployeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return new CurrentUserResponseDTO(
                employee.getId(),
                employee.getUsername(),
                employee.getFullName()
        );
    }
}
