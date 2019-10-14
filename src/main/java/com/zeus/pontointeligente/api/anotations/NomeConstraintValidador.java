package com.zeus.pontointeligente.api.anotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.util.Strings;

public class NomeConstraintValidador implements ConstraintValidator<ValidaNome, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (Strings.isBlank(value)) {
			return false;
		}
		return value.length() < 3 ? false : true;
	}

}
