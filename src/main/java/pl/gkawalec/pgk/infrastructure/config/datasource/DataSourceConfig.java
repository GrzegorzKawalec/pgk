package pl.gkawalec.pgk.infrastructure.config.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class DataSourceConfig {

    private final DataSourceBuilder dataSourceBuilder;

    @Bean
    @Primary
    public DataSource dataSource() {
        return dataSourceBuilder.build();
    }

}
