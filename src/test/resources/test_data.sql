-- Clean up existing data
DELETE FROM suggestions;
DELETE FROM users;
DELETE FROM admins;
DELETE FROM orgnizations;

-- Test data for organizations
INSERT INTO orgnizations (id, gmt_create, gmt_modified, org_name, address) VALUES
('org-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '测试组织1', '北京市海淀区'),
('org-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '测试组织2', '上海市浦东新区');

-- Test data for users
INSERT INTO users (wxid, user_name, user_org, user_phone, status, org_id, gmt_create, gmt_modified) VALUES
('user001', '测试用户1', '研发部', '13900139001', 1, 'org-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user002', '测试用户2', '产品部', '13900139002', 1, 'org-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user003', '测试用户3', '市场部', '13900139003', 1, 'org-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test data for admins
INSERT INTO admins (admin_wxid, admin_name, org_id, admin_phone, admin_passwd, gmt_create, gmt_modified) VALUES
('admin001', '管理员1', 'org-001', '13800138001', '$2a$10$X7Q8Y9Z0A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('admin002', '管理员2', 'org-002', '13800138002', '$2a$10$X7Q8Y9Z0A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Test data for suggestions
INSERT INTO suggestions (title, problem_description, problem_analysis, suggestion, expected_outcome, image_urls, user_wxid, status, org_id, gmt_create, gmt_modified) VALUES
('测试建议1', '测试问题描述1', '测试问题分析1', '测试建议内容1', '测试预期结果1', '["test1.jpg"]', 'user001', 0, 'org-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('测试建议2', '测试问题描述2', '测试问题分析2', '测试建议内容2', '测试预期结果2', '["test2.jpg"]', 'user002', 1, 'org-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('测试建议3', '测试问题描述3', '测试问题分析3', '测试建议内容3', '测试预期结果3', '["test3.jpg"]', 'user003', 2, 'org-002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 