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
public class InvalidCategoryException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -8548238934868802631L;
	private final String errorMessage;
	private final String errorCode;
	private final boolean status;

}
