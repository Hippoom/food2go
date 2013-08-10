-- Create table
create table t_f2g_restaurant
(
  ID number not null,
  NAME varchar2(1000) default '' not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_restaurant
  add constraint pk_f2g_restaurant primary key (ID);
  
--//@UNDO
drop table t_f2g_restaurant;
