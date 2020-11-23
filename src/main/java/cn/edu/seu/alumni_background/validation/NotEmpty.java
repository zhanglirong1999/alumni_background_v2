package cn.edu.seu.alumni_background.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = NotEmptyValidator.class)
public @interface NotEmpty {

    String message() default "当前对象是空或 null!";  // 必须有

    Class<?>[] groups() default {};  // 必须有

    Class<? extends Payload>[] payload() default {};  // 必须有
}
