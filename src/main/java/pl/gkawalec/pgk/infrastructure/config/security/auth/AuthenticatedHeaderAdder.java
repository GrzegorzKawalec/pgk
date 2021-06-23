package pl.gkawalec.pgk.infrastructure.config.security.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
class AuthenticatedHeaderAdder extends OncePerRequestFilter {

    private static final String HEADER_NAME = "Authenticated";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc) throws ServletException, IOException {
        String authenticatedUsername = getAuthenticatedUserName();
        res.addHeader(HEADER_NAME, authenticatedUsername);
        fc.doFilter(req, res);
    }

    private String getAuthenticatedUserName() {
        User authenticatedUser = AuthenticatedUserFetcher.getAuthenticatedUser();
        return Objects.isNull(authenticatedUser) ? "" :
                authenticatedUser.getUsername();
    }

}
