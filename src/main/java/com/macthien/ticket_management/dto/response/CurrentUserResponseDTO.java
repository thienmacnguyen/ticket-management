package com.macthien.ticket_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponseDTO {
    private Long id;
    private String username;
    private String fullName;

}
