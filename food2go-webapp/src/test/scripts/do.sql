
-- START CHANGE SCRIPT #1: 1_create_seq_f2g_pending_order.sql

-- Create sequence 
create sequence seq_f2g_pending_order
minvalue 1
maxvalue 99999999
start with 1
increment by 1
cache 20;



INSERT INTO changelog (change_number, complete_dt, applied_by, description)
 VALUES (1, CURRENT_TIMESTAMP, USER(), '1_create_seq_f2g_pending_order.sql');

COMMIT;

-- END CHANGE SCRIPT #1: 1_create_seq_f2g_pending_order.sql


-- START CHANGE SCRIPT #2: 2_create_t_f2g_pending_order.sql

-- Create table
create table t_f2g_pending_order
(
  TRACKING_ID number not null
  DELIVERY_ADDRESS_STREET1 varchar2(1000) default '' not null
  DELIVERY_ADDRESS_STREET2 varchar2(1000) default '' not null
  DELIVERY_TIME date not null
)
;
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_pending_order
  add constraint pk_f2g_pending_order primary key (TRACKING_ID);
  


INSERT INTO changelog (change_number, complete_dt, applied_by, description)
 VALUES (2, CURRENT_TIMESTAMP, USER(), '2_create_t_f2g_pending_order.sql');

COMMIT;

-- END CHANGE SCRIPT #2: 2_create_t_f2g_pending_order.sql

