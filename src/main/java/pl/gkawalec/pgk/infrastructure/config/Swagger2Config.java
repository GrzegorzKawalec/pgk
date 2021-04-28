package pl.gkawalec.pgk.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.gkawalec.pgk.infrastructure.constant.PGKProfiles;
import pl.gkawalec.pgk.infrastructure.setting.model.AppSetting;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import pl.gkawalec.pgk.PGKApplication;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
@Profile(PGKProfiles.DEV)
public class Swagger2Config {

    /**
     * http://localhost:${appSetting.getPort}/swagger-ui/
     * e.g. http://localhost:10003/swagger-ui/
     */

    private final AppSetting appSetting;

    private static final String BASE_PACKAGE = PGKApplication.class.getPackageName();

    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.regex(appSetting.getApiPrefix() + "/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(appSetting.getName())
                .description(appSetting.getName() + " REST API")
                .version(appSetting.getVersion())
                .description("Author: " + appSetting.getAuthor())
                .build();
    }

}
