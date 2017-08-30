package com.debitum.assets.port.adapter.helpers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public abstract class SpecificationFilter<T> implements Specification<T> {

    private final List<Specification<T>> specifications = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {
        if (specifications.isEmpty()) {
            return null;
        } else {
            Specification<T> result = specifications.get(0);
            for (int i = 1; i < specifications.size(); i++) {
                result = Specifications.where(result).and(specifications.get(i));
            }
            return result.toPredicate(root,
                    query,
                    cb);
        }
    }

    protected void addSpec(Specification<T> specification){
        this.specifications.add(specification);
    }

}