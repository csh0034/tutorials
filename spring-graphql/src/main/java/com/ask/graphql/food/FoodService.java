package com.ask.graphql.food;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {

  private final FoodRepository foodRepository;

  public Food save(String name) {
    return foodRepository.save(Food.create(name));
  }

  public Food findFood(String name) {
    return foodRepository.findByName(name)
        .orElseThrow(() -> new IllegalArgumentException("not found"));
  }

  public List<Food> findFoods() {
    return foodRepository.findAll();
  }

}
