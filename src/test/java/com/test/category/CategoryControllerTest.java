package com.test.category;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.category.model.CategoryVO;
import com.test.category.model.ErrorResponseVO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(classes = CategoryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@RunWith(SpringRunner.class)
class CategoryControllerTest {
	private int port=8001;

	@EventListener
	 void onWebInit(WebServerInitializedEvent event) {
	   port = event.getWebServer().getPort();

	 }


	private URL base;

	@Autowired
	private TestRestTemplate template;

	RequestSpecification reqSpec;

	Response response;

	Map<Long, String> map;

	@Test
	@Order(1)
	void testRootCategoryCreate() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.createCategoryFactory();
		ResponseEntity<CategoryVO> response = template.postForEntity("/api/v1/categories/addCategory", createCategoryRequest, CategoryVO.class);
		assertThat(response.getStatusCode().compareTo(HttpStatus.CREATED));
		assertNotNull(response.getBody());
	}

	@Test
	@Order(2)
	void testDuplicateRootCategoryCreate() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.createCategoryFactory();
		ResponseEntity<ErrorResponseVO> response = template.postForEntity("/api/v1/categories/addCategory", createCategoryRequest, ErrorResponseVO.class);
		assertThat(response.getStatusCode().compareTo(HttpStatus.CONFLICT));
		assertNotNull(response.getBody());
	}


	@Test
	@Order(3)
	void testAddNewSubCategory() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.createChildCategoryFactory("Footwear", 1);
		ResponseEntity<CategoryVO> response = template.postForEntity("/api/v1/categories/addCategory", createCategoryRequest, CategoryVO.class);
		assertThat(response.getStatusCode().compareTo(HttpStatus.CREATED));
		assertNotNull(response.getBody());
	}

	@Test
	@Order(4)
	void testAddNewSubCategoryToSubCategory() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.createChildCategoryFactory("Branded", 2);
		ResponseEntity<CategoryVO> response = template.postForEntity("/api/v1/categories/addCategory", createCategoryRequest, CategoryVO.class);
		assertThat(response.getStatusCode().compareTo(HttpStatus.CREATED));
		assertNotNull(response.getBody());
	}
	@Test
	@Order(5)
	void testDuplicateChild() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.createChildCategoryFactory("Branded", 2);
		ResponseEntity<ErrorResponseVO> response = template.postForEntity("/api/v1/categories/addCategory", createCategoryRequest, ErrorResponseVO.class);
		assertThat(response.getStatusCode().compareTo(HttpStatus.CONFLICT));
		assertNotNull(response.getBody());
	}

	public static Optional<Long> getKeysByValue(Map<Long, String> map, String value) {
		return map.entrySet().stream().filter(entry -> entry.getValue().equals(value)).map(Map.Entry::getKey)
				.findFirst();
	}

	@Test
	@Order(6)
	void testEditCategory() throws Exception {
		CategoryVO createCategoryRequest = CategoryMockFactory.editCategoryFactory(2, "Footwear-1", 1);

		reqSpec = new RequestSpecBuilder().setBody(createCategoryRequest).setBaseUri("http://localhost:" + port)
				.setContentType(ContentType.JSON).build();
		response = given().spec(reqSpec).when().put("/api/v1/categories/editCategory");
		System.out.println(response.asString());
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());

	}

	@Test
	@Order(7)
	void testDeleteCategory() throws Exception {
		reqSpec = new RequestSpecBuilder().setBaseUri("http://localhost:" + port)
				.setContentType(ContentType.JSON).build();
		response = given().spec(reqSpec).when().delete("/api/v1/categories/deleteCategory/1");
		System.out.println(response.asString());
		Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}

	@Test
	@Order(8)
	void testEditCategory_EmptyRequest() throws Exception {
		CategoryVO createCategoryRequest = new CategoryVO();
		reqSpec = new RequestSpecBuilder().setBody(createCategoryRequest).setBaseUri("http://localhost:" + port)
				.setContentType(ContentType.JSON).build();
		response = given().spec(reqSpec).when().put("/api/v1/categories/editCategory");
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}

	@Test
	@Order(9)
	void testDeleteCategory_EmptyRequest() throws Exception {
		reqSpec = new RequestSpecBuilder().setBody("").setBaseUri("http://localhost:" + port)
				.setContentType(ContentType.JSON).build();
		response = given().spec(reqSpec).when().delete("/api/v1/categories/deleteCategory/2444");
		Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}

	}
