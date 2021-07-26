package pl.gkawalec.pgk.api.dto.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import pl.gkawalec.pgk.common.annotation.request.NotAuditedRequestType;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.project.ProjectEntity;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@FieldNameConstants
@Setter(AccessLevel.PROTECTED)
public class ProjectBaseDTO {

    protected Long id;
    protected String name;

    @JsonIgnore
    @NotAuditedRequestType
    protected LocalDate dateStart;
    protected String dateStartStr;

    @JsonIgnore
    @NotAuditedRequestType
    protected LocalDate dateEnd;
    protected String dateEndStr;

    public LocalDate getDateStart() {
        if (Objects.isNull(dateStart)) {
            dateStart = DateTimeUtil.stringToLocalDate(dateStartStr);
        }
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
        this.dateStartStr = DateTimeUtil.localDateToString(dateStart);
    }

    public LocalDate getDateEnd() {
        if (Objects.isNull(dateEnd)) {
            dateEnd = DateTimeUtil.stringToLocalDate(dateEndStr);
        }
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
        this.dateEndStr = DateTimeUtil.localDateToString(dateEnd);
    }

    static ProjectBaseDTO of(ProjectEntity entity) {
        ProjectBaseDTO result = new ProjectBaseDTO();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setDateStart(entity.getDateStart());
        result.setDateEnd(entity.getDateEnd());
        return result;
    }

}
