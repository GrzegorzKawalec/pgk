package pl.gkawalec.pgk.database.project;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.user.UserEntity;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Audited
@FieldNameConstants
@Setter(AccessLevel.PACKAGE)
@Table(name = "pgk_project")
public class ProjectEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate dateStart;

    @NotNull
    private LocalDate dateEnd;

    @NotNull
    private boolean isActive = true;

    @NotNull
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id")
    private UserEntity projectManager;

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "pgk_project_participant_link",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants = new ArrayList<>();

    @NotNull
    @ManyToMany
    @JoinTable(
            name = "pgk_project_legal_act_link",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "legal_act_id")
    )
    private List<LegalActEntity> legalActs = new ArrayList<>();

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    void setName(String name) {
        this.name = StringUtil.trimAndRemoveMultipleWhiteSpace(name);
    }

    void setDescription(String description) {
        this.description = StringUtil.trim(description);
    }

}
