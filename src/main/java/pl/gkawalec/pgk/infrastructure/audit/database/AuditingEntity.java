package pl.gkawalec.pgk.infrastructure.audit.database;

import lombok.Getter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.gkawalec.pgk.common.exception.UpdatedEntityLockException;

import javax.persistence.*;

@Getter
@Audited
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingEntity {

    @Embedded
    @CreatedBy
    @AttributeOverride(name = "date", column = @Column(name = "created_on"))
    @AttributeOverride(name = "userId", column = @Column(name = "created_by"))
    private Auditor created;

    @Embedded
    @LastModifiedBy
    @AttributeOverride(name = "date", column = @Column(name = "last_modified_on"))
    @AttributeOverride(name = "userId", column = @Column(name = "last_modified_by"))
    private Auditor lastModified;

    @Version
    @Column(name = "entity_version")
    private int version = 1;

    public void setVersion(int version) {
        if (this.version > version) {
            throw new UpdatedEntityLockException(this.getClass(), this.version, version);
        }
        this.version = version;
    }

}
