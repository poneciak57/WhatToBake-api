package com.whattobake.api.Controller;

import com.whattobake.api.Dto.ProductFilters;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public Flux<Product> getAllProducts(@RequestBody Optional<ProductFilters> productFilters){
        return productService.getAllProducts(productFilters.orElse(new ProductFilters()).fillDefaults());
    }

}
