package pl.gkawalec.pgk.database.migration;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Entity
@Setter(AccessLevel.PACKAGE)
@Table(name = "migrations_pgk")
public class MigrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private Integer version;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    private LocalDateTime installedOn = LocalDateTime.now();

    @NotNull
    private Long executionTimeMs;

}
