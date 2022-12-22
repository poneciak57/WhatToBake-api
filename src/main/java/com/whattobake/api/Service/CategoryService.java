package com.whattobake.api.Service;

import com.whattobake.api.Model.Category;
import com.whattobake.api.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Flux<Category> allCategories(){
        return categoryRepository.findAll();
    }
}
