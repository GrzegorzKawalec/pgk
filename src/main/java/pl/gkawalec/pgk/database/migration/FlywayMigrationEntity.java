package pl.gkawalec.pgk.database.migration;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Immutable
@Table(name = "migrations_flyway")
public class FlywayMigrationEntity {

    @Id
    private Integer installedRank;

    private String version;

}
