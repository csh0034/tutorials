package com.ask.springr2dbc.repository;

import com.ask.springr2dbc.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

  private final DatabaseClient client;

  @Override
  public Flux<OrderVO> findAllOrderVO() {
    String query = "SELECT o.id as order_id, o.name as order_name, u.id as user_id, u.name as user_name"
        + " FROM mt_order as o JOIN mt_user as u ON o.user_id = u.id";

    return client.sql(query)
        .fetch()
        .all()
        .map(row -> {
          String orderId = (String) row.get("order_id");
          String orderName = (String) row.get("order_name");
          String userId = (String) row.get("user_id");
          String userName = (String) row.get("user_name");

          return new OrderVO(orderId, orderName, userId, userName);
        });
  }

}
