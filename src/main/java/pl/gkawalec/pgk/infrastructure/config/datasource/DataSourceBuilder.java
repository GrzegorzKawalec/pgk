package pl.gkawalec.pgk.infrastructure.config.datasource;

import com.p6spy.engine.spy.P6SpyDriver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;
import pl.gkawalec.pgk.application.setting.model.AppSetting;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
class DataSourceBuilder {

    private final AppSetting appSetting;
    private final DataSourceProperties dataSourceProperties;

    private static final String P6SPY_IN_URL = "p6spy:";

    DataSource build() {
        HikariDataSource dataSource = (HikariDataSource) dataSourceProperties.initializeDataSourceBuilder().build();
        dataSource.setMaximumPoolSize(appSetting.getDatabase().getMaxPoolSize());
        addP6Spy(dataSource);
        return dataSource;
    }

    private void addP6Spy(HikariDataSource dataSource) {
        String jdbcUrl = dataSource.getJdbcUrl();
        if (!appSetting.getDatabase().isP6spy() && jdbcUrl.contains(P6SPY_IN_URL)) {
            jdbcUrl = jdbcUrl.replace(P6SPY_IN_URL, "");
            dataSource.setJdbcUrl(jdbcUrl);
        }
        if (jdbcUrl.contains(P6SPY_IN_URL)) {
            String name = P6SpyDriver.class.getName();
            dataSource.setDriverClassName(name);
        }
    }

}
