-- 1. Lịch sử phân công của Ticket 1, mới nhất trước
SELECT * FROM ticket_assignment_history 
WHERE ticket_id = 1 
ORDER BY changed_at DESC;
-- 2. Đếm Ticket hiện được giao cho từng nhân viên
SELECT e.username, COUNT(t.id) 
FROM employees e LEFT JOIN tickets t ON e.id = t.assignee_id AND t.status != 'CLOSED'
GROUP BY e.id, e.username;
-- 3. Nhân viên có nhiều Ticket chưa đóng nhất
SELECT e.username, COUNT(t.id) as count
FROM employees e JOIN tickets t ON e.id = t.assignee_id WHERE t.status != 'CLOSED'
GROUP BY e.username ORDER BY count DESC LIMIT 1;

-- 4. Ticket đã phân công lại > 2 lần
SELECT ticket_id, COUNT(id) FROM ticket_assignment_history GROUP BY ticket_id HAVING COUNT(id) > 2;

-- 5. Ticket chưa từng được phân công
SELECT * FROM tickets WHERE assignee_id IS NULL AND id NOT IN (SELECT ticket_id FROM ticket_assignment_history);

-- 6. Lấy lần phân công gần nhất của mỗi Ticket
SELECT DISTINCT ON (ticket_id) * FROM ticket_assignment_history ORDER BY ticket_id, changed_at DESC;

-- 7. Ticket cùng tên reporter và assignee hiện tại
SELECT t.ticket_code FROM tickets t WHERE t.reporter_id = t.assignee_id;

-- 8. Nhân viên không được giao Ticket nào
SELECT e.username FROM employees e LEFT JOIN tickets t ON e.id = t.assignee_id WHERE t.id IS NULL;

-- 9. Viết INSERT vi phạm FOREIGN KEY
INSERT INTO ticket_assignment_history(ticket_id, new_assignee_id, changed_by) VALUES (9999, 1, 1);
-- Giải thích: Bắn lỗi "violates foreign key constraint". Do ticket_id 9999 không tồn tại.

-- 10. Viết INSERT vi phạm CHECK constraint
INSERT INTO tickets(ticket_code, title, priority, status, reporter_id) VALUES ('TK-009', 'A', 'LOW', 'DONE', 1);
-- Giải thích: Lỗi "violates check constraint". Chữ 'DONE' không nằm trong Enum 'OPEN, IN_PROGRESS, CLOSED...'.