package com.ask.mongodb.repository;

import com.ask.mongodb.domain.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String> {

}
