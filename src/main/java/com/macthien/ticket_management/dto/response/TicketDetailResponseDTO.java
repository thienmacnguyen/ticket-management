package com.macthien.ticket_management.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketDetailResponseDTO extends TicketResponseDTO {
    private List<TicketCommentResponseDTO> comments = new ArrayList<>();
    private List<TicketStatusHistoryResponseDTO> histories = new ArrayList<>();
}
