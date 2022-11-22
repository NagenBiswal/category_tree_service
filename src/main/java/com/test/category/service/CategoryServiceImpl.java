package com.test.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.category.constants.AppConstants;
import com.test.category.entiry.Category;
import com.test.category.exception.ApplicationException;
import com.test.category.exception.DuplicateCategoryException;
import com.test.category.exception.InvalidCategoryException;
import com.test.category.exception.RequiredException;
import com.test.category.model.CategoryVO;
import com.test.category.repo.CategoryRepository;
/**
 * 
 * @author Nagen
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<List<CategoryVO>> getCategroyTree() {
		Optional<List<Category>> rootCategories = Optional.ofNullable(repository.findByParentCategoryNull());
		if (rootCategories.isPresent() && rootCategories.get().size() > 0) {
			List<List<CategoryVO>> allList = new ArrayList<>();
			for (Category category : rootCategories.get()) {
				System.out.println(category.getName());
				System.out.println(category.getId());
				allList.add(getStructuredHierarchicalList(parseTreeOutput(category)));
			}
			return allList;
		} else {
			throw new ApplicationException(AppConstants.NO_DATA_FOUND_TEXT, AppConstants.NO_DATA_FOUND_TEXT, false);
		}
	}

	/**
	 *
	 * @param list
	 * @return
	 */
	public List<CategoryVO> getStructuredHierarchicalList(final List<CategoryVO> list) {
		final List<CategoryVO> copyList = new ArrayList<>(list);
		copyList.forEach(e -> {
			list.stream().filter(p -> p.categoryId == e.parentId).findAny().ifPresent(parent -> {
				if (parent.children == null) {
					parent.children = new ArrayList<>();
				}
				parent.children.add(e);
			});
		});
		list.subList(1, list.size()).clear();
		return list;
	}

	/**
	 *
	 * @param entity
	 * @return
	 */
	private List<CategoryVO> parseTreeOutput(Category entity) {
		List<CategoryVO> categoryVOs = new ArrayList<>();
		prepareNestedData(entity, categoryVOs); // call recursive
		return categoryVOs;
	}

	/**
	 *
	 * @param entity
	 * @param categoryVOs
	 */
	private void prepareNestedData(Category entity, List<CategoryVO> categoryVOs) {
		if (null == entity.getParentCategory()) {
			CategoryVO create = new CategoryVO(entity.getId(), entity.getName(), 0);
			categoryVOs.add(create);
		}
		if (entity.getChildren().size() > 0) {
			for (Category category : entity.getChildren()) {
				CategoryVO create = new CategoryVO(category.getId(), category.getName(),
						category.getParentCategory().getId());
				categoryVOs.add(create);
				prepareNestedData(category, categoryVOs);
			}
		}
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public CategoryVO addNewCategory(CategoryVO categoryVO) {
		if (categoryVO.getParentId() == 0) {
			Optional<Category> catOptionalRootCheck = Optional
					.ofNullable(repository.findByParentCategoryNullAndName(categoryVO.getName()));

			if (catOptionalRootCheck.isPresent()) {
				throw new DuplicateCategoryException(AppConstants.DUPLICATE_FOUND_TEXT,
						AppConstants.DUPLICATE_FOUND_ERROR_CODE, false);
			} else {
				Category category = new Category();
				category.setName(categoryVO.getName());
				Category categoryRoot = repository.save(category);
				return new CategoryVO(categoryRoot.getId(), categoryRoot.getName(), 0);
			}
		} else {
			Optional<Category> catOptionalChildCheck = Optional
					.ofNullable(repository.checkValidChildNative(categoryVO.getParentId(), categoryVO.getName()));
			if (catOptionalChildCheck.isPresent()) {
				throw new DuplicateCategoryException(AppConstants.DUPLICATE_FOUND_TEXT,
						AppConstants.DUPLICATE_FOUND_ERROR_CODE, false);
			} else {
				Category category = parsingCategoryData(categoryVO);
				Category categoryRoot = repository.save(category);
				return new CategoryVO(categoryRoot.getId(), categoryRoot.getName(),
						categoryRoot.getParentCategory().getId());
			}
		}
	}

	/**
	 *
	 * @param categoryVO
	 * @return
	 */
	private Category parsingCategoryData(CategoryVO categoryVO) {
		Category category = new Category();
		category.setName(categoryVO.getName());

		Category parentCategory = new Category();
		parentCategory.setId(categoryVO.getParentId());
		category.setParentCategory(parentCategory);

		return category;
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public CategoryVO editCategory(CategoryVO categoryVo) {
		// restrict the root category from edit
		if (categoryVo.getParentId() == 0 || categoryVo.getCategoryId() == 0) {
			throw new RequiredException(AppConstants.REQUIRED_PARENT_ID_TEXT,
					AppConstants.REQUIRED_PARENT_ID_ERROR_CODE, false);
		} else {
			// edit the category except root category
			Optional<Category> categoryOpt = repository.findById(categoryVo.getCategoryId());
			if (!categoryOpt.isPresent()) {
				throw new InvalidCategoryException(
						String.format(AppConstants.INVALID_ID_TEXT, categoryVo.getCategoryId()),
						AppConstants.INVALID_ID_ERROR_CODE, false);
			} else {
				categoryOpt.get().setName(categoryVo.getName());
				Category parentCategory = new Category();
				parentCategory.setId(categoryVo.getParentId());
				categoryOpt.get().setParentCategory(parentCategory);
				Category c = repository.save(categoryOpt.get());
				return new CategoryVO(c.getId(), c.getName(), c.getParentCategory().getId());
			}
		}
	}

	/**
	 *
	 */
	@Override
	@Transactional
	public String deleteCategory(Long categoryId) {

		try {
			Optional<Category> optional = repository.findById(categoryId);
			Category category = optional.get();
			category.removeChild(category);
			repository.delete(category);
			return AppConstants.DELETE_SUCCESS_TEXT;
		} catch (Exception e) {
			throw new ApplicationException(AppConstants.DELETE_FAILED_TEXT, AppConstants.DELETE_FAILED_ERROR_CODE,
					false);
		}

	}

}
