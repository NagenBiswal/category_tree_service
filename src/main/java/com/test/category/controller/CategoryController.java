package com.test.category.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.test.category.constants.AppConstants;
import com.test.category.exception.ApplicationException;
import com.test.category.exception.DuplicateCategoryException;
import com.test.category.exception.InvalidCategoryException;
import com.test.category.exception.RequiredException;
import com.test.category.model.CategoryVO;
import com.test.category.model.ErrorResponseVO;
import com.test.category.service.CategoryService;


/***
 *
 * @author Nagen 
 *
 */

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {

	private final CategoryService service;

	/***
	 * Constructor injection
	 *
	 * @param service
	 */
	@Autowired
	CategoryController(CategoryService service) {
		this.service = service;
	}

	/**
	 * This API is using for the test the rest service
	 *
	 * @return
	 */
	@GetMapping
	public String testApi() {
		return AppConstants.HELLO_TEXT;
	}

	/***
	 * This API is using for for get All the category list in tree structure hierarchy.
	 *
	 * @return
	 */
	@GetMapping("/")
	public List<List<CategoryVO>> getCategories() {
		return service.getCategroyTree();
	}

	/***
	 * addCategory API is using for create the root/child category. we can create
	 * multiple root by passing only category name {"name": "cat1"} It will restrict
	 * to duplicate the root category name We can add child category to any root
	 * category by passing any root category id in parent_id i.e. {"name":"cat45",
	 * "parentId":1} create the N number of child also N number of nested category.
	 *
	 * @param categoryVo
	 * @return
	 */
	@PostMapping("/addCategory")
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryVO addCategory(@RequestBody CategoryVO categoryVo) {

		Optional<String> name = Optional.ofNullable(categoryVo.getName());
		if (!name.isPresent()) {
			throw new RequiredException(AppConstants.EMPTY_CATEGORY_NAME_TEXT, AppConstants.EMPTY_CATEGORY_NAME_CODE,
					false);
		} else {
			return service.addNewCategory(categoryVo);
		}
	}

	/***
	 * editCategory API is using for the update the category except root category.
	 * Request: {"categroyId": 2, "name": "cat1", "parentId": 1}
	 *
	 * @param categoryVo
	 * @return
	 */
	@PutMapping("/editCategory")
	@ResponseStatus(HttpStatus.OK)
	public CategoryVO editCategory(@RequestBody CategoryVO categoryVo) {

		Optional<String> name = Optional.ofNullable(categoryVo.getName());
		if (!name.isPresent()) {
			throw new RequiredException(AppConstants.EMPTY_CATEGORY_NAME_TEXT, AppConstants.EMPTY_CATEGORY_NAME_CODE, false);
		} else {
			return service.editCategory(categoryVo);
		}
	}

	/***
	 * This API is using to delete the category.
	 * It will delete the nested category also
	 *
	 *
	 * @param categoryId
	 * @return
	 */
	@DeleteMapping("/deleteCategory/{categoryId}")
	@ResponseStatus(HttpStatus.OK)
	public String deleteCategory(@PathVariable String categoryId) {

		if (categoryId.isEmpty()) {
			throw new RequiredException(AppConstants.EMPTY_CATEGORY_ID_TEXT, AppConstants.EMPTY_CATEGORY_ID_CODE, false);
		} else {
			return service.deleteCategory(Long.parseLong(categoryId));
		}
	}

	@ExceptionHandler(RequiredException.class)
	public final ResponseEntity<ErrorResponseVO> handleAllExceptions(RequiredException e) {
		return new ResponseEntity<>(new ErrorResponseVO(e.getErrorMessage(), e.getErrorCode(), e.isStatus()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateCategoryException.class)
	public final ResponseEntity<ErrorResponseVO> handleAllExceptons(DuplicateCategoryException e) {
		return new ResponseEntity<>(new ErrorResponseVO(e.getErrorMessage(), e.getErrorCode(), e.isStatus()),HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCategoryException.class)
	public final ResponseEntity<ErrorResponseVO> handleAllExceptions(InvalidCategoryException e) {
		return new ResponseEntity<>(new ErrorResponseVO(e.getErrorMessage(), e.getErrorCode(), e.isStatus()),
				HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ApplicationException.class)
	public final ResponseEntity<ErrorResponseVO> handleAllExceptions(ApplicationException e) {
		return new ResponseEntity<>(new ErrorResponseVO(e.getErrorMessage(), e.getErrorCode(), e.isStatus()),
				HttpStatus.NOT_FOUND);
	}



}
