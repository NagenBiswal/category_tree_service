package com.test.category.service;

import java.util.List;

import com.test.category.model.CategoryVO;
/**
 * 
 * @author Nagen
 *
 */
public interface CategoryService {

	List<List<CategoryVO>> getCategroyTree();

	CategoryVO addNewCategory(CategoryVO categoryVO);

	CategoryVO editCategory(CategoryVO categoryVO);

	String deleteCategory(Long categoryId);

}
