package pl.gkawalec.pgk.common.annotation.security;

import pl.gkawalec.pgk.common.type.Authority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthGuard {
    Authority[] value();
}
