package com.whattobake.api.Integration;

import com.whattobake.api.Controller.CategoryController;
import com.whattobake.api.Dto.CategoryDto;
import com.whattobake.api.Dto.InsertDto.CategoryInsertRequest;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Service.CategoryService;
import com.whattobake.api.Util.Creators.CategoryCreator;
import com.whattobake.api.Util.Helpers.BaseIntegrationTestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@WebFluxTest(controllers = CategoryController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(CategoryService.class)
@Slf4j
public class CategoryTest extends BaseIntegrationTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testFindAll_whenCategoriesInDatabase_thenReturnListOfCategories() {
        Category category1 = createCategory();
        Category category2 = createCategory();

        webTestClient.get()
                .uri("/api/category")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CategoryDto.class)
                .hasSize(2)
                .contains(CategoryDto.fromCategory(category1), CategoryDto.fromCategory(category2));
    }

    @Test
    public void testFindOneById_whenExistsInDatabase_thenReturnCategory() {
        Category category = createCategory();

        webTestClient.get()
                .uri("/api/category/{id}", category.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryDto.class)
                .isEqualTo(CategoryDto.fromCategory(category));
    }

    @Test
    public void testFindOneById_whenDoesntExistInDatabase_thenReturnNotFoundError() {
        webTestClient.get()
                .uri("/api/category/{id}", CategoryCreator.INVALID_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.name").doesNotExist()
                .jsonPath("$.creation_date").doesNotExist()
                .jsonPath("$.message").exists();
    }

    @Test
    public void testCreate_whenValidPayload_thenReturnCategory() {
        CategoryInsertRequest request = new CategoryInsertRequest("New Category", "new-icon");

        webTestClient.post()
                .uri("/api/category")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryDto.class)
                .isEqualTo(CategoryDto.fromCategory(categoryRepository.findAll().blockFirst()));
        Assertions.assertEquals(1L, categoryRepository.count().block());
    }

    @Test
    public void testUpdate_whenCategoryExists_thenReturnCategory() {
        Category category = createCategory();
        CategoryInsertRequest categoryInsertRequest
                = new CategoryInsertRequest("updated", "updated");
        Category category_updated = Category.builder()
                .id(category.getId())
                .name(categoryInsertRequest.getName())
                .icon(categoryInsertRequest.getIcon())
                .build();
        webTestClient.put()
                .uri("/api/category/{id}", category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryInsertRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CategoryDto.class)
                .isEqualTo(CategoryDto.fromCategory(category_updated));
        Assertions.assertEquals(1L, categoryRepository.count().block());
    }
    @Test
    public void testUpdate_whenCategoryDoesntExist_thenReturnCategory() {
        CategoryInsertRequest categoryInsertRequest
                = new CategoryInsertRequest("updated", "updated");
        webTestClient.put()
                .uri("/api/category/{id}", CategoryCreator.INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoryInsertRequest)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.name").doesNotExist()
                .jsonPath("$.creation_date").doesNotExist()
                .jsonPath("$.message").exists();
        Assertions.assertEquals(0L, categoryRepository.count().block());
    }

    @Test
    public void testDelete_whenCategoryExists_thenReturnNothing() {
        Category category = createCategory();
        webTestClient.delete()
                .uri("/api/category/{id}", category.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
        Assertions.assertEquals(0L, categoryRepository.count().block());
    }

    @Test
    public void testDelete_whenCategoryDoesntExist_thenReturnNotFoundError() {
        Category category = createCategory();
        webTestClient.delete()
                .uri("/api/category/{id}", category.getId() + 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").exists();
        Assertions.assertEquals(1L, categoryRepository.count().block());
    }
}
