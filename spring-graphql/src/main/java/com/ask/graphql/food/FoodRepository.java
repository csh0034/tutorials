package com.ask.graphql.food;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface FoodRepository extends JpaRepository<Food, String> {

  Optional<Food> findByName(String name);

}
