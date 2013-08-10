-- Create table
create table t_f2g_restaurant_srv_area
(
  RESTAURANT_ID number not null,
  STREET varchar2(1000) default '' not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_restaurant_srv_area
  add constraint pk_f2g_restaurant_srv_area primary key (RESTAURANT_ID, STREET);
  
--//@UNDO
drop table t_f2g_restaurant_srv_area;
