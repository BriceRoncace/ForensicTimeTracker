package gov.idaho.isp.forensic.time.domain.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class CriteriaDate {
  public enum SearchType {
    ON("on"),
    AFTER("after"),
    BEFORE("before"),
    BETWEEN("between");

    private final String label;

    private SearchType (String label) {
      this.label = label;
    }

    public String getLabel() {
      return label;
    }
  }

  private LocalDate date1;
  private LocalDate date2;
  private SearchType searchType = SearchType.ON;

  public CriteriaDate() {}

  public CriteriaDate(LocalDate date1, SearchType searchType) {
    this.date1 = date1;
    this.searchType = searchType;
  }

  public CriteriaDate(LocalDate date1, LocalDate date2, SearchType searchType) {
    this.date1 = date1;
    this.date2 = date2;
    this.searchType = searchType;
  }

  public LocalDate getDate1() {
    return date1;
  }

  public void setDate1(LocalDate date1) {
    this.date1 = date1;
  }

  public LocalDate getDate2() {
    return date2;
  }

  public void setDate2(LocalDate date2) {
    this.date2 = date2;
  }

  public SearchType getSearchType() {
    return searchType;
  }

  public void setSearchType(SearchType searchType) {
    this.searchType = searchType;
  }

  public String getFilterText(String varName) {
    if (searchType != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(varName).append(" is ").append(searchType.getLabel());

      if (date1 != null) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        sb.append(" ");
        if (SearchType.BETWEEN == searchType && date2 != null) {
          sb.append(date1.format(formatter)).append(" and ").append(date2.format(formatter));
        }
        else {
          sb.append(date1.format(formatter));
        }
        return sb.toString();
      }
    }
    return null;
  }

  public boolean canBuildPredicate() {

    if (searchType == SearchType.BETWEEN && (date1 == null || date2 == null)) {
      return false;
    }

    return searchType != null && date1 != null;
  }

  public Predicate buildPredicate(CriteriaBuilder cb, Expression exp) {
    if (searchType == null) {
      throw new IllegalStateException("Cannot build predicate when CriteriaDate searchType is null");
    }
    if (searchType == SearchType.BETWEEN && date1 != null && date2 != null) {
      return cb.between(exp, date1, date2);
    }
    if (searchType == SearchType.ON && date1 != null) {
      return cb.equal(exp, date1);
    }
    if (searchType == SearchType.AFTER && date1 != null) {
      return cb.greaterThanOrEqualTo(exp, date1);
    }
    if (searchType == SearchType.BEFORE && date1 != null) {
      return cb.lessThanOrEqualTo(exp, date1);
    }
    return null;
  }

  public Predicate buildPredicateConvertToLocalDateTime(CriteriaBuilder cb, Expression exp) {
    if (searchType == null) {
      throw new IllegalStateException("Cannot build predicate when CriteriaDate searchType is null");
    }
    if (searchType == SearchType.BETWEEN && date1 != null && date2 != null) {
      return cb.between(exp, atStartOfDay(date1), atEndOfDay(date2));
    }
    if (searchType == SearchType.ON && date1 != null) {
      return cb.between(exp, atStartOfDay(date1), atEndOfDay(date1));
    }
    if (searchType == SearchType.AFTER && date1 != null) {
      return cb.greaterThanOrEqualTo(exp, atEndOfDay(date1));
    }
    if (searchType == SearchType.BEFORE && date1 != null) {
      return cb.lessThanOrEqualTo(exp, atStartOfDay(date1));
    }
    return null;
  }

  private LocalDateTime atStartOfDay(LocalDate ld) {
    return ld.atStartOfDay();
  }

  private LocalDateTime atEndOfDay(LocalDate ld) {
    return ld.atTime(LocalTime.MAX);
  }

  @Override
  public String toString() {
    return "CriteriaDate{" + "date1=" + date1 + ", date2=" + date2 + ", searchType=" + searchType + '}';
  }
}