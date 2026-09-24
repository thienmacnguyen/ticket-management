package com.macthien.ticket_management.service;

import com.macthien.ticket_management.dto.request.LoginDTO;
import com.macthien.ticket_management.dto.response.CurrentUserResponseDTO;
import com.macthien.ticket_management.dto.response.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO login(LoginDTO dto);

    CurrentUserResponseDTO getCurrentUser();
}
