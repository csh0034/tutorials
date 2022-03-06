package com.ask.springr2dbc.repository;

import com.ask.springr2dbc.vo.OrderVO;
import reactor.core.publisher.Flux;

public interface OrderRepositoryCustom {

  Flux<OrderVO> findAllOrderVO();

}
