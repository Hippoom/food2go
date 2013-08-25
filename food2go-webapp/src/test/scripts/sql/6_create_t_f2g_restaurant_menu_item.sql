-- Create table
create table t_f2g_restaurant_menu_item
(
  RESTAURANT_ID number not null,
  name varchar2(400) default '' not null,
  price number default 0.00 not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table t_f2g_restaurant_menu_item
  add constraint pk_f2g_restaurant_menu_item primary key (RESTAURANT_ID, name);
  
--//@UNDO
drop table t_f2g_restaurant_menu_item;
