-- This sql contains schema that cannot be auto-created by hibernate.

-- Create sequence 
create sequence seq_f2g_pending_order;

alter table t_f2g_order_line add constraint pk_f2g_order_line primary key (TRACKING_ID, name);

alter table t_f2g_restaurant_menu_item add constraint pk_f2g_restaurant_menu_item primary key (RESTAURANT_ID, name);