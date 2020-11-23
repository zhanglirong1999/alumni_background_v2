package cn.edu.seu.alumni_background.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {

    @Override
    public boolean isValid(
        Object o,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        return (o != null && !o.equals(""));
    }
}
