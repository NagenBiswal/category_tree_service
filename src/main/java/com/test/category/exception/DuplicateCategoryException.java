package com.test.category.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
/**
 * 
 * @author Nagen
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class DuplicateCategoryException extends RuntimeException {


	/**
	 *
	 */
	private static final long serialVersionUID = 1747849210337685792L;
	private final String errorMessage;
	private final String errorCode;
	private final boolean status;

}
