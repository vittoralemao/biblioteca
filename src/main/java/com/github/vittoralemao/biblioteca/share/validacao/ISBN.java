package com.github.vittoralemao.biblioteca.share.validacao;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ISBNValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ISBN {

    String message() default "ISBN inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
