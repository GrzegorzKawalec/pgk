package pl.gkawalec.pgk.common;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class Loggers {

    public final Logger AUDIT_REQUEST = LoggerFactory.getLogger("AUDIT_REQUEST");

}
