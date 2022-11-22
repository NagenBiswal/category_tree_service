package com.test.category.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
/**
 * 
 * @author Nagen
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category")
public class Category implements Serializable {

	private static final long serialVersionUID = 2316649522496279292L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Category parentCategory;

	@OneToMany(mappedBy = "parentCategory", cascade = { CascadeType.REMOVE, CascadeType.PERSIST }, orphanRemoval = false)
	private List<Category> children;

	public void removeChild(Category category) {
		children.remove(category);
		category.setParentCategory(null);
	}

	public void addChild(Category category) {
		children.remove(category);
		category.setParentCategory(this);
	}

}
