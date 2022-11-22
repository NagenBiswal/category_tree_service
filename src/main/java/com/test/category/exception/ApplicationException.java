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
public class ApplicationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -3671237188616523783L;
	private final String errorMessage;
	private final String errorCode;
	private final boolean status;


}
