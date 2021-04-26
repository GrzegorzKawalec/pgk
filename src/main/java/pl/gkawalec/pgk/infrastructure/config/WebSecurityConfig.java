package pl.gkawalec.pgk.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppSetting appSetting;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .authorizeRequests().antMatchers(appSetting.getSecurity().getUnauthorizedRequests()).permitAll()
                .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        String[] ignoringWebAppPatterns = ignoringWebAppPatterns().toArray(new String[0]);
        web.ignoring().antMatchers(ignoringWebAppPatterns);
    }

    private List<String> ignoringWebAppPatterns() {
        List<String> ignoringWebAppPatterns = new ArrayList<>(List.of(
                "/",
                "/**/*.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.scss",
                "/assets/**"
        ));
        if (appSetting.getProfile().equals("dev")) {
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
