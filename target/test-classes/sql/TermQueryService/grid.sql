SELECT 
  t.*,
  s.sub_city_name 
FROM
  (SELECT 
    t.net_id,
    t.tenancy_point,
    a.countA,
    b.countB
  FROM
    net_manage_tbl t,
    (SELECT 
      COUNT(*) countA,
      rent_netid 
    FROM
      (SELECT 
        host_seri,
        tran_date,
        tran_time,
        oper_id,
        CARD_TYPE,
        rent_netid,
        TRAN_FLAG,
        user_cost 
      FROM
        tran_log_view 
      UNION
      SELECT 
        host_seri,
        tran_date,
        tran_time,
        oper_id,
        CARD_TYPE,
        rent_netid,
        TRAN_FLAG,
        user_cost 
      FROM
        term_tran_log_tbl_his) 
    WHERE tran_date between '' and '' 
    GROUP BY rent_netid) a,
    (SELECT 
      COUNT(*) countB,
      return_netid 
    FROM
      (SELECT 
        host_seri,
        return_date,
        return_time,
        oper_id,
        CARD_TYPE,
        return_netid,
        TRAN_FLAG,
        user_cost 
      FROM
        tran_log_view 
      UNION
      SELECT 
        host_seri,
        return_date,
        return_time,
        oper_id,
        CARD_TYPE,
        return_netid,
        TRAN_FLAG,
        user_cost 
      FROM
        term_tran_log_tbl_his) 
    WHERE return_datetime between ? and ?
      AND (
        tran_flag = '2' 
        OR tran_flag = '5' 
        OR tran_flag = '7'
      ) 
    GROUP BY return_netid) b 
  WHERE t.net_id = rent_netid (+) 
    AND t.net_id = return_netid (+)) t,
  net_manage_tbl n,
  sub_city_corr_tbl s 
WHERE t.net_id = n.net_id (+) 
  AND " & nettxt & " 
  AND n.corp_name = s.sub_city (+) 
ORDER BY s.sub_city_name,t.net_id 