package com.whattobake.api.Integration;

import com.whattobake.api.Controller.TagController;
import com.whattobake.api.Model.Category;
import com.whattobake.api.Model.Product;
import com.whattobake.api.Model.Tag;
import com.whattobake.api.Service.TagService;
import com.whattobake.api.Util.Creators.TagCreator;
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
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest(controllers = TagController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@Import(TagService.class)
@Slf4j
public class TagTest extends BaseIntegrationTestHelper {

    @DynamicPropertySource
    public static void neo4jProperties(DynamicPropertyRegistry registry) {
        connectToNeo4jContainer(registry);
    }

    @Test
    public void testFindAll_whenTagsInDatabase_thenReturnListOfTags() {
        Category category = createCategory();
        Product product = createProduct(category);
        Tag tag = createTag();
        log.info("data test: {}", tag.getCreationDate());
        webTestClient.get()
                .uri("/api/tag")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(tag.getId())
                .jsonPath("$.[0].name").isEqualTo(tag.getName())
                .jsonPath("$.[0].creation_date").doesNotExist()
                .jsonPath("$.[1]").doesNotExist()
                .jsonPath("$.[1]").doesNotHaveJsonPath();
    }

    @Test
    public void testFindOneById_whenExistsInDatabase_thenReturnListOfTags() {
        Category category = createCategory();
        Product product = createProduct(category);
        Tag tag1 = createTag();
        Tag tag2 = createTag();
        webTestClient.get()
                .uri("/api/tag/{id}", tag2.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(tag2.getId())
                .jsonPath("$.name").isEqualTo(tag2.getName())
                .jsonPath("$.creation_date").doesNotExist();
    }

    @Test
    public void testFindOneById_whenTagDoesntExist_thenReturnNotFoundError() {
        webTestClient.get()
                .uri("/api/tag/{id}", TagCreator.INVALID_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.name").doesNotExist()
                .jsonPath("$.creation_date").doesNotExist()
                .jsonPath("$.message").exists();
    }

    @Test
    public void testCreate_whenValidPayload_thenReturnTag() {
        webTestClient.post()
                .uri("/api/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        "name": "test_name"
                        }
                        """))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNumber()
                .jsonPath("$.name").isEqualTo("test_name")
                .jsonPath("$.creation_date").doesNotExist();
        Assertions.assertEquals(1L, tagRepository.count().block());
    }

    @Test
    public void testCreate_whenInvalidPayload_thenReturnError() {
        webTestClient.post()
                .uri("/api/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        "name": ""
                        }
                        """))
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.name").doesNotExist()
                .jsonPath("$.creation_date").doesNotExist();
        Assertions.assertEquals(0L, tagRepository.count().block());
    }

    @Test
    public void testUpdate_whenTagExist_thenReturnTag() {
        Tag tag = createTag();
        webTestClient.put()
                .uri("/api/tag/{id}", tag.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        "name": "test_name_updated"
                        }
                        """))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(tag.getId())
                .jsonPath("$.name").isEqualTo("test_name_updated")
                .jsonPath("$.creation_date").doesNotExist();
        Assertions.assertEquals(1L, tagRepository.count().block());
    }

    @Test
    public void testUpdate_whenTagDoesntExist_thenReturnNotFoundError() {
        webTestClient.put()
                .uri("/api/tag/{id}", TagCreator.INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("""
                        {
                        "name": "test_name_updated"
                        }
                        """))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.id").doesNotExist()
                .jsonPath("$.name").doesNotExist()
                .jsonPath("$.creation_date").doesNotExist()
                .jsonPath("$.message").exists();
        Assertions.assertEquals(0L, tagRepository.count().block());
    }

    @Test
    public void testDelete_whenTagExists_thenReturnNothing() {
        Tag tag = createTag();
        webTestClient.delete()
                .uri("/api/tag/{id}", tag.getId())
                .exchange()
                .expectStatus().isOk();
        Assertions.assertEquals(0L, tagRepository.count().block());
    }

    @Test
    public void testDelete_whenTagDoesntExist_thenReturnNotFoundError() {
        Tag tag = createTag();
        webTestClient.delete()
                .uri("/api/tag/{id}", tag.getId() + 1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").exists();
        Assertions.assertEquals(1L, tagRepository.count().block());
    }
}
