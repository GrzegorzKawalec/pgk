package pl.gkawalec.pgk.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppSetting appSetting;

    @Override
    public void configure(WebSecurity web) {
        String[] ignoringWebAppPatterns = ignoringWebAppPatterns().toArray(new String[0]);
        web.ignoring().antMatchers(ignoringWebAppPatterns);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureAuthenticationEntryPoint(http);
        configureAuthorizeRequests(http);
        configureCSRF(http);
    }

    private void configureAuthenticationEntryPoint(HttpSecurity http) throws Exception {
        HttpStatusEntryPoint unauthorizedEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
    }

    private void configureAuthorizeRequests(HttpSecurity httpSecurity) throws Exception {
        String[] unauthorizedRequests = appSetting.getSecurity().getUnauthorizedRequests();
        httpSecurity.authorizeRequests()
                .antMatchers(unauthorizedRequests).permitAll()
                .anyRequest().authenticated();
    }

    private void configureCSRF(HttpSecurity http) throws Exception {
        if (isDevProfile()) {
            http.csrf().disable();
        } else {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }


    private List<String> ignoringWebAppPatterns() {
        List<String> ignoringWebAppPatterns = new ArrayList<>(List.of(
                "/",
                "/**/*.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.scss",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.ttf",
                "/assets/**"
        ));
        if (isDevProfile()) {
            ignoringWebAppPatterns.addAll(ignoringWebSwaggerPatterns());
        }
        return ignoringWebAppPatterns;
    }

    private List<String> ignoringWebSwaggerPatterns() {
        return List.of(
                "/swagger-ui/**",
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    private boolean isDevProfile() {
        return "dev".equals(appSetting.getProfile());
    }

}
