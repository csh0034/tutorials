package com.ask.graphql.food;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class FoodController {

  private final FoodService foodService;

  @MutationMapping
  public Food save(@Argument String name) {
    return foodService.save(name);
  }

  @QueryMapping
  public Food findFood(@Argument String name) {
    return foodService.findFood(name);
  }

  @QueryMapping
  public List<Food> findFoods() {
    return foodService.findFoods();
  }

}
