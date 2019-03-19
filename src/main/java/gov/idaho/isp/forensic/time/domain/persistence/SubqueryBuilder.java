package gov.idaho.isp.forensic.time.domain.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.ListAttribute;

public class SubqueryBuilder<T> {
  private final Subquery<T> subquery;
  private final Root subqueryRoot;
  private final List<Predicate> predicates = new ArrayList<>();

  public SubqueryBuilder(CriteriaQuery<?> cq, Class<T> clazz) {
    subquery = cq.subquery(clazz);
    subqueryRoot = subquery.from(clazz);
    subquery.select(subqueryRoot);
  }

  public Root getRoot() {
    return subqueryRoot;
  }

  public Join getJoin(ListAttribute la) {
    return subqueryRoot.join(la, JoinType.LEFT);
  }

  public void addPredicate(Predicate predicate) {
    predicates.add(predicate);
  }

  public void addAllPredicates(List<Predicate> predicates) {
    this.predicates.addAll(predicates);
  }

  public Subquery<T> build(CriteriaBuilder cb, PredicateReducer predicateReducer) {
    subquery.where(predicateReducer.reduce(cb, predicates));
    return subquery;
  }
}
