package com.test.category;

import com.test.category.model.CategoryVO;
/**
 * 
 * @author Nagen
 *
 */
public class CategoryMockFactory {

	public CategoryMockFactory() {
	}

	public static CategoryVO createCategoryFactory() {
		CategoryVO vo = new CategoryVO();
		vo.setName("Men");
		return vo;
	}

	public static CategoryVO createChildCategoryFactory(String name, long parentId) {
		CategoryVO vo = new CategoryVO();
		vo.setName(name);
		vo.setParentId(parentId);
		return vo;
	}

	public static CategoryVO editCategoryFactory(int categoryId, String name, int parentID) {
		CategoryVO vo = new CategoryVO();
		vo.setName(name);
		vo.setParentId(parentID);
		vo.setCategoryId(categoryId);
		return vo;
	}

	public static CategoryVO deleteCategoryFactory(int categoryId, String name) {
		CategoryVO vo = new CategoryVO();
		vo.setName(name);
		vo.setCategoryId(categoryId);
		return vo;
	}

}
