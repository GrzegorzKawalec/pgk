package pl.gkawalec.pgk.infrastructure.migration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface PGKMigrationAnnotation {
}
