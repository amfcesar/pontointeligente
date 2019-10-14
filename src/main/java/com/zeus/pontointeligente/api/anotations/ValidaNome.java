package com.zeus.pontointeligente.api.anotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NomeConstraintValidador.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidaNome {
	String message() default "Nome não pode conter menos que 3 caracteres";
	Class<?>[] groups() default{};
	Class<? extends Payload>[] payload() default {};
}
