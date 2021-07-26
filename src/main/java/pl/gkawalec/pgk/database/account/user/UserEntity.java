package pl.gkawalec.pgk.database.account.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Audited
@Table(name = "pgk_user")
@Setter(AccessLevel.PACKAGE)
public class UserEntity extends BaseUserEntity {

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

}
