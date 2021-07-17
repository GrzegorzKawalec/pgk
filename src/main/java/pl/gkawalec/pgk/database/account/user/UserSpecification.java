package pl.gkawalec.pgk.database.account.user;

import org.springframework.data.jpa.domain.Specification;
import pl.gkawalec.pgk.api.dto.account.user.UserCriteria;
import pl.gkawalec.pgk.common.user.AnonymousUserAccessor;
import pl.gkawalec.pgk.common.user.SystemUserAccessor;
import pl.gkawalec.pgk.common.util.CollectionUtil;
import pl.gkawalec.pgk.common.util.PredicateUtil;
import pl.gkawalec.pgk.common.util.StringUtil;
import pl.gkawalec.pgk.database.account.role.RoleEntity_;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

public record UserSpecification(UserCriteria criteria) implements Specification<UserSearchEntity> {

    @Override
    @ParametersAreNonnullByDefault
    public Predicate toPredicate(Root<UserSearchEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesOr = new ArrayList<>();

        isActive(root, criteriaBuilder, predicates);
        excludedUsers(root, criteriaBuilder, predicates);

        addSearchBy(root, criteriaBuilder, predicatesOr);
        addRoles(root, predicatesOr);
        if (CollectionUtil.isNotEmpty(predicatesOr)) {
            Predicate or = PredicateUtil.togetherOr(predicatesOr, criteriaBuilder);
            predicates.add(or);
        }

        criteriaQuery.distinct(true);
        return PredicateUtil.togetherAnd(predicates, criteriaBuilder);
    }

    private void isActive(Root<UserSearchEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        Path<Boolean> isActivePath = root.get(BaseUserEntity_.isActive);
        Predicate isActivePredicate = Boolean.TRUE.equals(criteria.getIsActive()) ?
                criteriaBuilder.isTrue(isActivePath) :
                criteriaBuilder.isFalse(isActivePath);
        predicates.add(isActivePredicate);
    }

    private void excludedUsers(Root<UserSearchEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        Path<String> emailPath = root.get(BaseUserEntity_.email);
        Predicate anonymousUserPredicate = criteriaBuilder.notLike(emailPath, AnonymousUserAccessor.IDENTIFIER);
        Predicate systemUserPredicate = criteriaBuilder.notLike(emailPath, SystemUserAccessor.IDENTIFIER);
        predicates.add(anonymousUserPredicate);
        predicates.add(systemUserPredicate);
    }

    private void addSearchBy(Root<UserSearchEntity> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
        if (StringUtil.isBlank(criteria.getSearchBy())) {
            return;
        }
        predicates.add(searchLikeBy(root, criteriaBuilder, BaseUserEntity_.email));
        predicates.add(searchLikeBy(root, criteriaBuilder, BaseUserEntity_.firstName));
        predicates.add(searchLikeBy(root, criteriaBuilder, BaseUserEntity_.lastName));
        predicates.add(searchLikeBy(root, criteriaBuilder, BaseUserEntity_.phoneNumber));
    }

    private Predicate searchLikeBy(Root<UserSearchEntity> root, CriteriaBuilder criteriaBuilder, SingularAttribute<BaseUserEntity, String> column) {
        Path<String> path = root.get(column);
        return PredicateUtil.like(criteria.getSearchBy(), path, criteriaBuilder);
    }

    private void addRoles(Root<UserSearchEntity> root, List<Predicate> predicates) {
        if (CollectionUtil.isEmpty(criteria.getRoleIds())) {
            return;
        }
        Predicate inRoleIds = root
                .join(UserSearchEntity_.credentials)
                .join(UserCredentialsEntity_.role)
                .get(RoleEntity_.id)
                .in(criteria.getRoleIds());
        predicates.add(inRoleIds);
    }

}
