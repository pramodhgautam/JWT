package com.nchl.merchantbusiness.repository.specification;

import com.nchl.merchantbusiness.model.CreditorRole;
import com.nchl.merchantbusiness.model.CreditorUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CreditorRoleSpecification {

    public static Specification<CreditorRole> byId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<CreditorRole> byName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

}
