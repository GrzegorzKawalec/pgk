package pl.gkawalec.pgk.infrastructure.config.security;

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
import pl.gkawalec.pgk.common.utils.ProfileUtil;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class PGKWebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String UNIVERSAL_PASS = "kgp";
    public static final String URL_SIGN_IN = AppSetting.API_PREFIX + "/sign-in";
    public static final String URL_SIGN_OUT = AppSetting.API_PREFIX + "/sign-out";

    private final AppSetting appSetting;
    private final ProfileUtil profileUtil;

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
        configureSignIn(http);
        configureSignOut(http);
    }

    private void configureAuthenticationEntryPoint(HttpSecurity http) throws Exception {
        HttpStatusEntryPoint unauthorizedEntryPoint = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint);
    }

    private void configureAuthorizeRequests(HttpSecurity http) throws Exception {
        String[] unauthorizedRequests = appSetting.getSecurity().getUnauthorizedRequests();
        http.authorizeRequests()
                .antMatchers(unauthorizedRequests).permitAll()
                .anyRequest().authenticated();
    }

    private void configureCSRF(HttpSecurity http) throws Exception {
        if (profileUtil.isDevProfile() || profileUtil.isAutomaticTestProfile()) {
            http.csrf().disable();
        } else {
            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        }
    }

    private void configureSignIn(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginProcessingUrl(URL_SIGN_IN)
                .successHandler((request, response, authentication) -> response.setStatus(HttpStatus.OK.value()))
                .failureHandler((request, response, exception) -> response.sendError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()))
                .usernameParameter("email")
                .passwordParameter("password");
    }

    private void configureSignOut(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl(URL_SIGN_OUT)
                .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpStatus.OK.value()));
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
        if (profileUtil.isDevProfile()) {
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

}
