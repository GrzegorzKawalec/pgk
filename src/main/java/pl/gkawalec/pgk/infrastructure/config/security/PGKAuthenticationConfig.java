package pl.gkawalec.pgk.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class PGKAuthenticationConfig {

    private static final String QUERY_USER = "" +
            " select uc.email, uc.password, true" +
            " from pgk_user_credentials uc" +
            " where uc.email = ?";

    private static final String QUERY_AUTHORITIES = "" +
            " select uc.email, a.authority" +
            " from pgk_user_credentials uc inner join pgk_role r on r.id = uc.role_id" +
            " inner join pgk_role_authority_link link on link.role_id = r.id" +
            " inner join pgk_authority a on link.authority_id = a.id" +
            " where uc.email = ?";

    private final DataSource dataSource;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider)
                .jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery(QUERY_USER)
                .authoritiesByUsernameQuery(QUERY_AUTHORITIES);
    }

}
