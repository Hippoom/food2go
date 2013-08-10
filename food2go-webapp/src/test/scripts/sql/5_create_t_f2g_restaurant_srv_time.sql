-- Create table
create table t_f2g_restaurant_srv_time
(
  RESTAURANT_ID number not null,
  Day varchar2(3) default '' not null,
  TIME_RANGE_START varchar2(5) default '' not null,
  TIME_RANGE_END varchar2(5) default '' not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_restaurant_srv_time
  add constraint pk_f2g_restaurant_srv_time primary key (RESTAURANT_ID, Day, TIME_RANGE_START);
  
--//@UNDO
drop table t_f2g_restaurant_srv_time;
