package com.macthien.ticket_management.service;

import com.macthien.ticket_management.dto.request.LoginDTO;
import com.macthien.ticket_management.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginDTO dto);
}
