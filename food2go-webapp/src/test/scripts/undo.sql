
-- START UNDO OF CHANGE SCRIPT #2: 2_create_t_f2g_pending_order.sql

drop table t_f2g_pending_order;


DELETE FROM changelog WHERE change_number = 2;

COMMIT;

-- END UNDO OF CHANGE SCRIPT #2: 2_create_t_f2g_pending_order.sql


-- START UNDO OF CHANGE SCRIPT #1: 1_create_seq_f2g_pending_order.sql

drop sequence seq_f2g_pending_order;


DELETE FROM changelog WHERE change_number = 1;

COMMIT;

-- END UNDO OF CHANGE SCRIPT #1: 1_create_seq_f2g_pending_order.sql

