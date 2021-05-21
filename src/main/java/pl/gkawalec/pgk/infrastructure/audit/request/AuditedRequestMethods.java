package pl.gkawalec.pgk.infrastructure.audit.request;

import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

@UtilityClass
class AuditedRequestMethods {

    final ImmutableList<RequestMethod> AUDIT_REQUEST_METHODS = ImmutableList.of(
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH
    );

    final ImmutableList<Class<? extends Annotation>> AUDIT_MAPPING_ANNOTATION = ImmutableList.of(
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            PatchMapping.class
    );


}
