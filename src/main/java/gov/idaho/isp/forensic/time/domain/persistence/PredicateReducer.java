package gov.idaho.isp.forensic.time.domain.persistence;

import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

public enum PredicateReducer {
  AND {
    @Override
    public Predicate reduce(CriteriaBuilder cb, List<Predicate> predicates) {
      return cb.and(predicates.toArray(new Predicate[0]));
    }
  },
  OR {
    @Override
    public Predicate reduce(CriteriaBuilder cb, List<Predicate> predicates) {
      return cb.or(predicates.toArray(new Predicate[0]));
    }
  };

  public abstract Predicate reduce(CriteriaBuilder cb, List<Predicate> predicates);

  public Predicate reduce(CriteriaBuilder cb, Predicate... predicates) {
    return reduce(cb, Arrays.asList(predicates));
  }
}