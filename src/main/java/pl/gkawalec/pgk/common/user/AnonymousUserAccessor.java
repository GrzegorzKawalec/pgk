package pl.gkawalec.pgk.common.user;

import org.springframework.stereotype.Component;

@Component
public class AnonymousUserAccessor extends InternalUser {

    public static final String IDENTIFIER = "*anonymous*";

    @Override
    String getIdentifier() {
        return IDENTIFIER;
    }

}
