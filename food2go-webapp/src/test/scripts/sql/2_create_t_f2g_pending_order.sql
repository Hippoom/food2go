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
  
--//@UNDO
drop table t_f2g_pending_order;
