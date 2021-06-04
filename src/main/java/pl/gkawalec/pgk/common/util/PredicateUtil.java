package pl.gkawalec.pgk.common.util;

import lombok.experimental.UtilityClass;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class PredicateUtil {

    public Predicate like(String searchBy, Expression<String> column, CriteriaBuilder criteriaBuilder) {
        searchBy = Objects.isNull(searchBy) ? "" : searchBy.trim().toLowerCase();
        searchBy = searchBy.replace("/", "//");
        searchBy = searchBy.replace("%", "/%");
        searchBy = searchBy.replace("_", "/_");
        searchBy = "%" + searchBy + "%";
        return criteriaBuilder.like(criteriaBuilder.lower(column), searchBy, '/');
    }

    public Predicate togetherAnd(List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
        if (CollectionUtil.isEmpty(predicates)) {
            return null;
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public Predicate togetherOr(List<Predicate> predicates, CriteriaBuilder criteriaBuilder) {
        if (CollectionUtil.isEmpty(predicates)) {
            return null;
        }
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }

}
