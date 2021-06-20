package pl.gkawalec.pgk.database.account.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Entity
@Audited
@Table(name = "pgk_user")
@Setter(AccessLevel.PACKAGE)
public class UserEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(max = 50)
    private String firstName;

    @NotNull
    @Size(max = 50)
    private String lastName;

    @Size(max = 20)
    private String phoneNumber;

    private String description;

    @NotNull
    private boolean isActive = true;

    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    void setEmail(String email) {
        this.email = StringUtil.trim(email);
    }

    void setFirstName(String firstName) {
        this.firstName = StringUtil.trim(firstName);
    }

    void setLastName(String lastName) {
        this.lastName = StringUtil.trim(lastName);
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = StringUtil.trim(phoneNumber);
    }

    void setDescription(String description) {
        this.description = StringUtil.trim(description);
    }

}
