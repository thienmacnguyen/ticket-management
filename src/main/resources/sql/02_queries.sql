INSERT INTO employees (username, full_name, email) VALUES 
('thienmac', 'Mạc Thiên', 'thien@gmail.com'),
('nguyenvana', 'Nguyễn Văn A', 'a@gmail.com'),
('tranvanb', 'Trần Văn B', 'b@gmail.com');

INSERT INTO tickets (ticket_code, title, description, priority, status, reporter_id, assignee_id, created_at, resolved_at) VALUES 
('TK-001', 'Lỗi mạng', 'Mất kết nối', 'HIGH', 'OPEN', 1, NULL, NOW() - INTERVAL '10 days', NULL),
('TK-002', 'Xin cấp RAM', 'Máy lag', 'MEDIUM', 'IN_PROGRESS', 2, 1, NOW() - INTERVAL '5 days', NULL),
('TK-003', 'Hỏng chuột', 'Chuột double click', 'LOW', 'RESOLVED', 2, 1, NOW() - INTERVAL '40 days', NOW() - INTERVAL '35 days'),
('TK-004', 'Lỗi server', 'Server down', 'URGENT', 'RESOLVED', 2, 1, NOW() - INTERVAL '15 days', NOW() - INTERVAL '12 days'),
('TK-005', 'Màn hình xanh', 'BSOD liên tục', 'URGENT', 'CLOSED', 1, 2, NOW(), NULL);

INSERT INTO ticket_status_history (ticket_id, from_status, to_status, changed_by, changed_at) VALUES 
(4, 'OPEN', 'IN_PROGRESS', 1, NOW() - INTERVAL '14 days'),
(4, 'IN_PROGRESS', 'RESOLVED', 1, NOW() - INTERVAL '12 days'),
(1, 'OPEN', 'IN_PROGRESS', 2, NOW()); -- Cố tình tạo dữ liệu lỗi lệch với status hiện tại (OPEN)

INSERT INTO ticket_comments (ticket_id, author_id, content, created_at)
SELECT 1, 1, 'Comment ' || gs, NOW() - INTERVAL '5 days' FROM generate_series(1, 6) AS gs;

/* CÂU 1: Liệt kê ticket chưa đóng, kèm reporter và assignee */
SELECT t.ticket_code, t.title, t.status, 
       r.username AS reporter_name, 
       a.username AS assignee_name
FROM tickets t
JOIN employees r ON t.reporter_id = r.id
LEFT JOIN employees a ON t.assignee_id = a.id
WHERE t.status != 'CLOSED';

/* CÂU 2: Đếm số ticket theo từng status và priority */
SELECT status, priority, COUNT(*) as total_tickets
FROM tickets
GROUP BY status, priority
ORDER BY status, priority;
/* CÂU 3: Tìm top 3 assignee có nhiều ticket RESOLVED nhất trong 30 ngày qua */
SELECT e.username, COUNT(t.id) as resolved_count
FROM tickets t
JOIN employees e ON t.assignee_id = e.id
WHERE t.status = 'RESOLVED' 
  AND t.resolved_at >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY e.username
ORDER BY resolved_count DESC
LIMIT 3;

/* CÂU 4: Liệt kê employee chưa từng được phân công ticket */
SELECT e.username, e.email
FROM employees e
LEFT JOIN tickets t ON e.id = t.assignee_id
WHERE t.id IS NULL;

/*
   CÂU 5: Tính thời gian xử lý trung bình theo assignee
*/
SELECT e.username, 
       AVG(EXTRACT(EPOCH FROM (t.resolved_at - t.created_at))/3600) AS avg_hours_taken
FROM tickets t
JOIN employees e ON t.assignee_id = e.id
WHERE t.status IN ('RESOLVED', 'CLOSED') 
  AND t.resolved_at IS NOT NULL
GROUP BY e.username;

/*
   CÂU 6: Tìm ticket có status hiện tại không khớp lịch sử mới nhất
*/
WITH LatestHistory AS (
    SELECT ticket_id, to_status,
           ROW_NUMBER() OVER(PARTITION BY ticket_id ORDER BY changed_at DESC) as rn
    FROM ticket_status_history
)
SELECT t.ticket_code, t.status AS current_status, lh.to_status AS history_status
FROM tickets t
JOIN LatestHistory lh ON t.id = lh.ticket_id AND lh.rn = 1
WHERE t.status != lh.to_status;


/* 
   CÂU 7: Tìm ticket > 5 comment và comment gần nhất quá 3 ngày
*/
SELECT t.ticket_code, t.title, COUNT(tc.id) AS total_comments, MAX(tc.created_at) AS latest_comment_time
FROM tickets t
JOIN ticket_comments tc ON t.id = tc.ticket_id
GROUP BY t.id, t.ticket_code, t.title
HAVING COUNT(tc.id) > 5 
   AND MAX(tc.created_at) < NOW() - INTERVAL '3 days';

/*
   CÂU 8: Top nhân viên tạo (Reporter) nhiều Ticket nhất
*/
SELECT e.username, COUNT(t.id) as tickets_reported
FROM employees e
JOIN tickets t ON e.id = t.reporter_id
GROUP BY e.id, e.username
HAVING COUNT(t.id) >= 2
ORDER BY tickets_reported DESC;