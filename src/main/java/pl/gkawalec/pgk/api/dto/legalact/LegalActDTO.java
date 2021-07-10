package pl.gkawalec.pgk.api.dto.legalact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import pl.gkawalec.pgk.common.util.DateTimeUtil;
import pl.gkawalec.pgk.database.legalact.LegalActEntity;

import java.time.LocalDate;
import java.util.Objects;

@Data
@FieldNameConstants
@Setter(AccessLevel.NONE)
@Builder(access = AccessLevel.PRIVATE)
public class LegalActDTO {

    private Long id;
    private String name;
    private String link;
    private String description;

    private Integer entityVersion;

    @JsonIgnore
    private LocalDate dateOf;
    private String dateOfStr;

    public LocalDate getDateOf() {
        if (Objects.isNull(dateOf)) {
            dateOf = DateTimeUtil.stringToLocalDate(dateOfStr);
        }
        return dateOf;
    }

    private void setDateOf(LocalDate dateOf) {
        this.dateOf = dateOf;
        this.dateOfStr = DateTimeUtil.localDateToString(dateOf);
    }

    public static LegalActDTO of(LegalActEntity entity) {
        LegalActDTO result = LegalActDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .link(entity.getLink())
                .description(entity.getDescription())
                .entityVersion(entity.getVersion())
                .build();
        result.setDateOf(entity.getDateOf());
        return result;
    }

}
