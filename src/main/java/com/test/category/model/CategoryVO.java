package com.test.category.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author Nagen
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryVO {
	public long categoryId;
	private String name;
	public long parentId;
	public List<CategoryVO> children;



	public CategoryVO(long categoryId, String name, long parentId) {
		this(categoryId, name, parentId, null);
	}

}
