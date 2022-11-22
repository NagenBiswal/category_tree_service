package com.test.category.model;

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
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseVO {
	private String errorMessage;
	private String errorCode;
	private boolean status;
}
