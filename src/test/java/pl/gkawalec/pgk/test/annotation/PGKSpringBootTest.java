package pl.gkawalec.pgk.test.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import pl.gkawalec.pgk.test.annotation.PGKTestProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootTest
@PGKTestProfiles
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PGKSpringBootTest {
}
