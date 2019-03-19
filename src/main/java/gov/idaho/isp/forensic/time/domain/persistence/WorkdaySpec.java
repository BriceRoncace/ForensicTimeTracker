package gov.idaho.isp.forensic.time.domain.persistence;

import gov.idaho.isp.forensic.time.domain.Category;
import gov.idaho.isp.forensic.time.domain.County;
import gov.idaho.isp.forensic.time.domain.CourtTestimony;
import gov.idaho.isp.forensic.time.domain.CourtTestimony_;
import gov.idaho.isp.forensic.time.domain.Discipline;
import gov.idaho.isp.forensic.time.domain.Entry;
import gov.idaho.isp.forensic.time.domain.Entry_;
import gov.idaho.isp.forensic.time.domain.Hours;
import gov.idaho.isp.forensic.time.domain.Hours_;
import gov.idaho.isp.forensic.time.domain.Lab;
import gov.idaho.isp.forensic.time.domain.WorkEntry;
import gov.idaho.isp.forensic.time.domain.WorkEntry_;
import gov.idaho.isp.forensic.time.domain.Workday;
import gov.idaho.isp.forensic.time.domain.Workday_;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class WorkdaySpec implements Specification<Workday> {
  private boolean submitted = false;
  private String username;

  private String firstName;
  private String lastName;
  private CriteriaDate workingDate = new CriteriaDate();
  private String caseNumber;

  private List<Lab> labs = new ArrayList<>();
  private List<Discipline> disciplines = new ArrayList<>();
  private List<County> counties = new ArrayList<>();
  private List<Category> categories = new ArrayList<>();

  @Override
  public Predicate toPredicate(Root<Workday> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    if (StringUtils.isNotBlank(username)) {
      predicates.add(cb.equal(cb.lower(root.get(Workday_.username)), username.toLowerCase()));
    }
    if (StringUtils.isNotBlank(firstName)) {
      predicates.add(cb.like(cb.lower(root.get(Workday_.firstName)), firstName.toLowerCase() + "%"));
    }
    if (StringUtils.isNotBlank(lastName)) {
      predicates.add(cb.like(cb.lower(root.get(Workday_.lastName)), lastName.toLowerCase() + "%"));
    }
    if (workingDate.canBuildPredicate()) {
      predicates.add(workingDate.buildPredicate(cb, root.get(Workday_.workdayDate)));
    }
    if (!labs.isEmpty()) {
      predicates.add(root.get(Workday_.lab).in(labs));
    }

    if (StringUtils.isNotBlank(caseNumber) || !counties.isEmpty()) {
      predicates.add(getCourtTestimonyPredicate(root, cq, cb));
    }
    if (!disciplines.isEmpty()) {
      predicates.add(getDisciplinePredicate(root, cq, cb));
    }
    if (!categories.isEmpty()) {
      predicates.add(getCategoryPredicate(root, cq, cb));
    }

    return PredicateReducer.AND.reduce(cb, predicates);
  }

  private Predicate getCategoryPredicate(Root<Workday> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    SubqueryBuilder<Workday> sqBuilder = new SubqueryBuilder(cq, Workday.class);

    if (categories.stream().anyMatch(c -> c.isFor(WorkEntry.class))) {
      Join<WorkEntry, Entry> workEntryJoin = sqBuilder.getJoin(Workday_.workEntries).join(WorkEntry_.entries, JoinType.LEFT);
      Join<Entry, Hours> workEntryHoursJoin = workEntryJoin.join(Entry_.hours, JoinType.LEFT);
      sqBuilder.addAllPredicates(buildCategoriesForClass(cb, workEntryJoin, workEntryHoursJoin, WorkEntry.class));
    }

    if (categories.stream().anyMatch(c -> c.isFor(CourtTestimony.class))) {
      Join<CourtTestimony, Entry> courtEntryJoin = sqBuilder.getJoin(Workday_.courtTestimonies).join(CourtTestimony_.entries, JoinType.LEFT);
      Join<Entry, Hours> courtEntryHoursJoin = courtEntryJoin.join(Entry_.hours, JoinType.LEFT);
      sqBuilder.addAllPredicates(buildCategoriesForClass(cb, courtEntryJoin, courtEntryHoursJoin, CourtTestimony.class));
    }

    return cb.in(root).value(sqBuilder.build(cb, PredicateReducer.OR));
  }

  private List<Predicate> buildCategoriesForClass(CriteriaBuilder cb, Join entryJoin, Join hoursJoin, Class categoryClass) {
    List<Predicate> predicates = new ArrayList<>();
    categories.stream().filter(c -> c.isFor(categoryClass)).forEach(c -> {
      Predicate category = cb.equal(entryJoin.get(Entry_.category), c);
      Predicate normalGT = cb.greaterThan(hoursJoin.get(Hours_.normal), BigDecimal.ZERO);
      Predicate overtimeGT = cb.greaterThan(hoursJoin.get(Hours_.overtime), BigDecimal.ZERO);

      Predicate orPredicate = PredicateReducer.OR.reduce(cb, normalGT, overtimeGT);
      predicates.add(PredicateReducer.AND.reduce(cb, category, orPredicate));
    });
    return predicates;
  }

  private Predicate getDisciplinePredicate(Root<Workday> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    SubqueryBuilder<Workday> sqBuilder = new SubqueryBuilder(cq, Workday.class);
    Join<Workday, WorkEntry> workEntryJoin = sqBuilder.getJoin(Workday_.workEntries);
    Join<Workday, CourtTestimony> courtTestimonyJoin = sqBuilder.getJoin(Workday_.courtTestimonies);

    sqBuilder.addPredicate(workEntryJoin.get(WorkEntry_.discipline).in(disciplines));
    sqBuilder.addPredicate(courtTestimonyJoin.get(CourtTestimony_.discipline).in(disciplines));

    return cb.in(root).value(sqBuilder.build(cb, PredicateReducer.OR));
  }

  private Predicate getCourtTestimonyPredicate(Root<Workday> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
    SubqueryBuilder<Workday> sqBuilder = new SubqueryBuilder(cq, Workday.class);
    Join<Workday, CourtTestimony> courtTestimonyJoin = sqBuilder.getJoin(Workday_.courtTestimonies);

    if (StringUtils.isNotBlank(caseNumber)) {
      sqBuilder.addPredicate(cb.isMember(caseNumber.toUpperCase(), courtTestimonyJoin.get(CourtTestimony_.caseNumbers)));
    }
    if (!counties.isEmpty()) {
      sqBuilder.addPredicate(courtTestimonyJoin.get(CourtTestimony_.county).in(counties));
    }

    return cb.in(root).value(sqBuilder.build(cb, PredicateReducer.AND));
  }

  public boolean isSubmitted() {
    return submitted;
  }

  public void setSubmitted(boolean submitted) {
    this.submitted = submitted;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CriteriaDate getWorkingDate() {
    return workingDate;
  }

  public void setWorkingDate(CriteriaDate workingDate) {
    this.workingDate = workingDate;
  }

  public String getCaseNumber() {
    return caseNumber;
  }

  public void setCaseNumber(String caseNumber) {
    this.caseNumber = caseNumber;
  }

  public List<Lab> getLabs() {
    return labs;
  }

  public void setLabs(List<Lab> labs) {
    this.labs = labs;
  }

  public List<Discipline> getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(List<Discipline> disciplines) {
    this.disciplines = disciplines;
  }

  public List<County> getCounties() {
    return counties;
  }

  public void setCounties(List<County> counties) {
    this.counties = counties;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }
}
