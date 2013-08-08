-- Create sequence 
create sequence seq_f2g_pending_order
minvalue 1
maxvalue 99999999
start with 1
increment by 1
cache 20;

--//@UNDO
drop sequence seq_f2g_pending_order;
