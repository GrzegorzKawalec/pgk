package pl.gkawalec.pgk.common.user;

import org.springframework.stereotype.Component;

@Component
public class SystemUserAccessor extends InternalUser {

    public static final String IDENTIFIER = "*system*";

    @Override
    String getIdentifier() {
        return IDENTIFIER;
    }

}
