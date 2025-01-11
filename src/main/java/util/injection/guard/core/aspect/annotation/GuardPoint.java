package util.injection.guard.core.aspect.annotation;

import util.injection.guard.core.aspect.annotation.element.GuardType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GuardPoint {

    GuardType point() default GuardType.GUARD_TYPE_DUAL;
}
