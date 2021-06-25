package pl.gkawalec.pgk.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class TestConverterJSONUtil {

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    private final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();

    public String convert(Object obj) throws JsonProcessingException {
        if (Objects.isNull(obj)) {
            return null;
        }
        return OBJECT_WRITER.writeValueAsString(obj);
    }

}
