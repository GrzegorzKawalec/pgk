package pl.gkawalec.pgk.database.legalact;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Entity
@Audited
@FieldNameConstants
@Setter(AccessLevel.PACKAGE)
@Table(
        name = "pgk_legal_act",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "date_of"})
)
public class LegalActEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "date_of")
    private LocalDate dateOf;

    @NotNull
    @Column(unique = true)
    private String link;

    @NotNull
    @Column(name = "is_active")
    private boolean active = true;

    private String description;

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

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
