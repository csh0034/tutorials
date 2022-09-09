package com.ask.kitchenpos.product.ui;

import com.ask.kitchenpos.product.application.ProductService;
import com.ask.kitchenpos.product.domain.Product;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<String> create(@RequestBody Product request) {
    String productId = productService.create(request);
    return ResponseEntity.created(URI.create("/api/products/" + productId))
        .body(productId);
  }

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    return ResponseEntity.ok(productService.findAll());
  }

}
