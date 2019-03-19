package gov.idaho.isp.forensic.time.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;

@Entity
public class Entry implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Valid
  private Hours hours = new Hours();

  public Entry() {}

  public Entry(Category category) {
    this.category = category;
  }

  public boolean isEmpty() {
    return hours == null || hours.isEmpty();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Hours getHours() {
    return hours;
  }

  public void setHours(Hours hours) {
    this.hours = hours;
  }

  @Override
  public String toString() {
    return "Entry{" + "id=" + id + ", category=" + category + ", hours=" + hours + '}';
  }
}
