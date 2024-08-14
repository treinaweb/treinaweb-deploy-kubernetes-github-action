package br.com.treinaweb.twjobs.core.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SkillNameIsUniqueValidator.class)
public @interface SkillNameIsUnique {

    String message() default "this ${validatedValue} skill name is already in use";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
    
}
