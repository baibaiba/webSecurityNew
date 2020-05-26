package demo.securitystarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {
    /**
     * 角色名称
     *
     * @return
     */
    Role name() default Role.STAFF;

    enum Role {
        STAFF,
        ADMIN
    }
}
