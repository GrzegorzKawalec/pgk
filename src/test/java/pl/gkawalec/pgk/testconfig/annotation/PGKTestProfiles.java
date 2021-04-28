package pl.gkawalec.pgk.testconfig.annotation;

import org.springframework.test.context.ActiveProfiles;
import pl.gkawalec.pgk.infrastructure.constant.PGKProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles(PGKProfiles.TEST_A)
public @interface PGKTestProfiles {
}
