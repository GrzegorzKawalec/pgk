package pl.gkawalec.pgk.database.account.user;

import lombok.Getter;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.infrastructure.audit.database.AuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@MappedSuperclass
public abstract class BaseUserEntity extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true)
    protected String email;

    @NotNull
    @Size(max = 50)
    protected String firstName;

    @NotNull
    @Size(max = 50)
    protected String lastName;

    @Size(max = 20)
    protected String phoneNumber;

    protected String description;

    @NotNull
    protected boolean isActive = true;

    protected void setEmail(String email) {
        this.email = StringUtil.trim(email);
    }

    protected void setFirstName(String firstName) {
        this.firstName = StringUtil.trim(firstName);
    }

    protected void setLastName(String lastName) {
        this.lastName = StringUtil.trim(lastName);
    }

    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = StringUtil.trim(phoneNumber);
    }

    protected void setDescription(String description) {
        this.description = StringUtil.trim(description);
    }

}
