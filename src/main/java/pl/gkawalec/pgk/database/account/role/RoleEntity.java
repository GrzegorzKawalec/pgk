package pl.gkawalec.pgk.database.account.role;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.authority.AuthorityEntity;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Audited
@Table(name = "pgk_role")
@Setter(AccessLevel.PACKAGE)
public class RoleEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pgk_role_authority_link",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private List<AuthorityEntity> authorities = new ArrayList<>();

    void setName(String name) {
        this.name = StringUtil.trimAndRemoveMultipleWhiteSpace(name);
    }

    void setDescription(String description) {
        this.description = StringUtil.trim(description);
    }

}
