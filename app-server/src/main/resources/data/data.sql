-- =====================================================================
-- MyOffice dev seed data
--
-- Plain-text passwords (for local login only):
--   admin / admin123
--   alice / bob / carol / david / erin   →  123456
--
-- Hashes are produced by BCrypt (cost=10). Spring Security's
-- BCryptPasswordEncoder accepts both `$2a$` and `$2b$` prefixes.
-- This script is idempotent: it clears the three tables before inserting.
-- =====================================================================

-- Reset tables (DELETE so AUTO_INCREMENT counters are reset by the explicit ids)
DELETE FROM `task`;
DELETE FROM `user`;
DELETE FROM `admin`;

-- ----- admin -----
INSERT INTO `admin` (`id`, `name`, `password`) VALUES
  (1, 'admin', '$2b$10$RnaMorcV8vfNhbCAVqmEUeMgvAjwThCnyGRgBoetfx7NH6UHBnZCO');

-- ----- user -----
-- All five share the same plain-text password `123456`.
INSERT INTO `user` (`id`, `username`, `password`) VALUES
  (1, 'alice', '$2b$10$ZmR2MOxlzmN23BoFkPs9v.cbdnWByFrlnHf53YkHZg26grn1dPtV.'),
  (2, 'bob',   '$2b$10$ZmR2MOxlzmN23BoFkPs9v.cbdnWByFrlnHf53YkHZg26grn1dPtV.'),
  (3, 'carol', '$2b$10$ZmR2MOxlzmN23BoFkPs9v.cbdnWByFrlnHf53YkHZg26grn1dPtV.'),
  (4, 'david', '$2b$10$ZmR2MOxlzmN23BoFkPs9v.cbdnWByFrlnHf53YkHZg26grn1dPtV.'),
  (5, 'erin',  '$2b$10$ZmR2MOxlzmN23BoFkPs9v.cbdnWByFrlnHf53YkHZg26grn1dPtV.');

-- ----- task -----
-- 10 records covering every status (0/1/2/3), every templateFormat (docx/xlsx/pptx)
-- and every templateSource (BLANK/UPLOAD/SELECT). The `template` fileId values are
-- placeholders for listing/UI; to actually edit a document, create a new task via
-- the UI (BLANK/UPLOAD) which will materialize a real file under app-server's
-- `${user.dir}/files/<fileId>/`.
INSERT INTO `task`
  (`id`, `name`, `description`, `template`, `template_name`, `template_source`,
   `template_format`, `start_date`, `end_date`, `owner`, `assignee`, `status`)
VALUES
  ( 1, 'Q2 季度汇报',         '准备 Q2 季度汇报材料并发送给管理层评审',
    'seed-task-1', 'Q2汇报.docx',     'BLANK',  'docx', '2026-05-01', '2026-05-20', 1, '1,2',     1),

  ( 2, '销售数据统计',         '汇总本月销售明细并生成报表',
    'seed-task-2', '销售统计.xlsx',   'BLANK',  'xlsx', '2026-05-05', '2026-05-25', 2, '2,3,4',   0),

  ( 3, '产品宣讲 PPT',         '为下周客户演示准备产品宣讲幻灯片',
    'seed-task-3', '产品宣讲.pptx',   'BLANK',  'pptx', '2026-05-08', '2026-05-22', 3, '1,3,5',   1),

  ( 4, '年度合同模板',         '基于法务版合同模板编制 2026 年度版本',
    'seed-task-4', '合同模板2026.docx', 'UPLOAD', 'docx', '2026-04-10', '2026-04-30', 1, '1',       2),

  ( 5, '客户满意度问卷',       '设计并下发客户满意度调研问卷',
    'seed-task-5', '客户问卷.docx',   'BLANK',  'docx', '2026-03-01', '2026-03-15', 4, '4,5',     2),

  ( 6, '财务对账单',           '与财务确认本季度对账单（已归档）',
    'seed-task-6', '对账单Q1.xlsx',   'UPLOAD', 'xlsx', '2026-01-05', '2026-01-31', 2, '1,2',     3),

  ( 7, '团队周报模板',         '建立周报通用模板，便于团队复用',
    'seed-task-7', '周报模板.docx',   'SELECT', 'docx', '2026-04-20', '2026-05-31', 5, '1,2,3,4,5', 1),

  ( 8, 'OKR 复盘',             '上半年 OKR 复盘材料',
    'seed-task-8', 'OKR复盘.pptx',    'BLANK',  'pptx', '2026-05-12', '2026-06-10', 3, '2,3',     0),

  ( 9, '开发进度跟踪',         '研发周进度跟踪表（迭代中）',
    'seed-task-9', '研发进度.xlsx',   'UPLOAD', 'xlsx', '2026-05-02', '2026-06-30', 1, '1,3,5',   1),

  (10, '历史项目存档',         '项目结项归档说明',
    'seed-task-10','项目存档.docx',   'SELECT', 'docx', '2025-12-01', '2025-12-20', 4, '4',       3);
