package pl.gkawalec.pgk.testconfig.annotation;

import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ActiveProfiles("test_a")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PGKTestProfiles {
}
