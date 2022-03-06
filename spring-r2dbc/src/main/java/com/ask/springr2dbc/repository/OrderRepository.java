package com.ask.springr2dbc.repository;

import com.ask.springr2dbc.model.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepository extends R2dbcRepository<Order, String>, OrderRepositoryCustom {

}
