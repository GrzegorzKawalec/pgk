package pl.gkawalec.pgk.infrastructure.audit.request;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import pl.gkawalec.pgk.common.annotation.request.NotAuditedRequestType;

import java.util.Objects;

@UtilityClass
class JSONConverter {

    private final Gson GSON = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            NotAuditedRequestType annotation = f.getAnnotation(NotAuditedRequestType.class);
            return Objects.nonNull(annotation);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            NotAuditedRequestType annotation = clazz.getAnnotation(NotAuditedRequestType.class);
            return Objects.nonNull(annotation);
        }
    }).create();

    String toJSON(Object toJSON) {
        return Objects.isNull(toJSON) ? null :
                GSON.toJson(toJSON);
    }

}
