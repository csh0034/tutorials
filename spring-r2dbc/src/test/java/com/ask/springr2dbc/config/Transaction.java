package com.ask.springr2dbc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Transaction {

  private static TransactionalOperator rxtx;

  @Autowired
  public Transaction(final TransactionalOperator rxtx) {
    Transaction.rxtx = rxtx;
  }

  public static <T> Mono<T> withRollback(final Mono<T> publisher) {
    return rxtx.execute(tx -> {
          tx.setRollbackOnly();
          return publisher;
        })
        .next();
  }

  public static <T> Flux<T> withRollback(final Flux<T> publisher) {
    return rxtx.execute(tx -> {
      tx.setRollbackOnly();
      return publisher;
    });
  }

}
