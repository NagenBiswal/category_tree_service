package com.test.category.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.test.category.entiry.Category;
/**
 * 
 * @author Nagen
 *
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByParentCategoryNull();

	Category findByParentCategoryNullAndName(String name);

	@Query(value = "SELECT * FROM Category c WHERE c.parent_id = ?1 and c.name = ?2",nativeQuery = true )
	Category checkValidChildNative(Long parentId, String name);

	Category findByName(String name);

}
