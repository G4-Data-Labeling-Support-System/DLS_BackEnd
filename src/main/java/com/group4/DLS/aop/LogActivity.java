package com.group4.DLS.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.group4.DLS.domain.enums.ActionType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogActivity {
    
    String action();
    String entity();
    String description() default "";

    // Which parameter contains entityId
    String entityIdParam() default ""; // For UPDATE/DELETE
    String entityIdField() default "id"; // For CREATE (from response)
}
