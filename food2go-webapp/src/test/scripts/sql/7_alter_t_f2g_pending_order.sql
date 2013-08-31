alter table t_f2g_pending_order
  add column restaurant_id number;
  
--//@UNDO
alter table t_f2g_pending_order
  drop column restaurant_id;
