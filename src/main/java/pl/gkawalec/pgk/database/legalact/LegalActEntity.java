package pl.gkawalec.pgk.database.legalact;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Entity
@Audited
@Setter(AccessLevel.PACKAGE)
@Table(
        name = "pgk_legal_act",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "dateOf"})
)
public class LegalActEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dateOf;

    @NotNull
    @Column(unique = true)
    private String link;

    private String description;

    void setName(String name) {
        this.name = StringUtil.trimAndRemoveMultipleWhiteSpace(name);
    }

    void setLink(String link) {
        this.link = StringUtil.trim(link);
    }

    void setDescription(String description) {
        this.description = StringUtil.trim(description);
    }

}
