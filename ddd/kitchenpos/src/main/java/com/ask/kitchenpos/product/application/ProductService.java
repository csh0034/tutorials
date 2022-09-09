package com.ask.kitchenpos.product.application;

import com.ask.kitchenpos.product.domain.Product;
import com.ask.kitchenpos.product.domain.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Transactional
  public String create(Product request) {
    Product product = productRepository.save(request);
    return product.getId();
  }

  @Transactional(readOnly = true)
  public List<Product> findAll() {
    return productRepository.findAll();
  }

}
