package com.macthien.ticket_management;

import com.macthien.ticket_management.enums.ErrorCode;
import com.macthien.ticket_management.exception.AppException;
import com.macthien.ticket_management.service.Impl.TicketServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class validateActorTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    @DisplayName("Test 1: actorIdFromRequest bị null -> Ném IllegalArgumentException")
    void testValidateActor_WhenRequestActorIdIsNull_ThrowsIllegalArgumentException() {
        Long requestActorId = null;
        Long tokenActorId = 1L;

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.validateActor(requestActorId, tokenActorId, false);
        });
    }

    @Test
    @DisplayName("Test 2: actorIdFromToken bị null -> Ném IllegalArgumentException")
    void testValidateActor_WhenTokenActorIdIsNull_ThrowsIllegalArgumentException() {
        Long requestActorId = 1L;
        Long tokenActorId = null;

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.validateActor(requestActorId, tokenActorId, false);
        });
    }

    @Test
    @DisplayName("Test 3: Hai ID khác nhau -> Ném AppException với lỗi FORBIDDEN (403)")
    void testValidateActor_WhenIdsDoNotMatch_ThrowsForbiddenAppException() {
        Long requestActorId = 2L; // Body gửi ID = 2
        Long tokenActorId = 1L;    // Token thực tế là ID = 1

        AppException exception = assertThrows(AppException.class, () -> {
            ticketService.validateActor(requestActorId, tokenActorId, false);
        });

        assertEquals(ErrorCode.FORBIDDEN, exception.getErrorCode());
    }

    @Test
    @DisplayName("Test 4: Hai ID giống nhau (kể cả ngoài khoảng cache Long) -> Thành công không ném ngoại lệ")
    void testValidateActor_WhenIdsMatch_Success() {
        // Dùng 1000L để kiểm tra vượt khoảng cache -128 đến 127 của Java
        Long requestActorId = 1000L;
        Long tokenActorId = 1000L;

        assertDoesNotThrow(() -> {
            ticketService.validateActor(requestActorId, tokenActorId, false);
        });
    }
}