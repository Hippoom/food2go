-- Create table
create table t_f2g_order_line
(
  Tracking_ID number not null,
  name varchar2(400) default '' not null,
  price number default 0.00 not null,
  quantity number(3) default 0 not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_order_line
  add constraint pk_f2g_order_line primary key (Tracking_ID, name);
  
--//@UNDO
drop table t_f2g_order_line;
