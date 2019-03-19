package gov.idaho.isp.forensic.time.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames={"username", "workdayDate"})})
public class Workday implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  private String firstName;

  private String middleName;

  private String lastName;

  private String username;

  private LocalDate workdayDate;

  @NotNull(message = "{lab.null}")
  @Enumerated(EnumType.STRING)
  private Lab lab;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "workdayId")
  @Valid
  private List<WorkEntry> workEntries = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "workdayId")
  @Valid
  private List<CourtTestimony> courtTestimonies = new ArrayList<>();

  public Workday() {}

  public Workday(LocalDate workdayDate) {
    this.workdayDate = workdayDate;
  }

  public Workday(User user) {
    this.firstName = user.getFirstName();
    this.middleName = user.getMiddleName();
    this.lastName = user.getLastName();
    this.username = user.getUsername();
    this.workEntries.add(new WorkEntry());
  }

  public BigDecimal getNormalHoursTotal() {
    return getAllHours().stream().map(c -> c.getNormal()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.FLOOR);
  }

  public BigDecimal getOvertimeHoursTotal() {
    return getAllHours().stream().map(h -> h.getOvertime()).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.FLOOR);
  }

  public DayOfWeek getDayOfWeek() {
    return workdayDate.getDayOfWeek();
  }

  @Max(value = 24, message = "{dayTotal.max}")
  private BigDecimal getDayTotal() {
    return getNormalHoursTotal().add(getOvertimeHoursTotal());
  }

  private List<Hours> getAllHours() {
    List<Hours> hours = workEntries.stream().flatMap(we -> we.getAllHours().stream()).collect(Collectors.toList());
    hours.addAll(courtTestimonies.stream().flatMap(ct -> ct.getAllHours().stream()).collect(Collectors.toList()));
    return hours;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName == null ? "" : firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName == null ? "" : middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName == null ? "" : lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDisplayName() {
    return getLastName() + ", " + getFirstName() + " " + getMiddleName();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LocalDate getWorkdayDate() {
    return workdayDate;
  }

  public void setWorkdayDate(LocalDate workdayDate) {
    this.workdayDate = workdayDate;
  }

  public Lab getLab() {
    return lab;
  }

  public void setLab(Lab lab) {
    this.lab = lab;
  }

  public List<WorkEntry> getWorkEntries() {
    return workEntries;
  }

  public void setWorkEntries(List<WorkEntry> workEntries) {
    this.workEntries = workEntries;
  }

  public List<CourtTestimony> getCourtTestimonies() {
    return courtTestimonies;
  }

  public void setCourtTestimonies(List<CourtTestimony> courtTestimonies) {
    this.courtTestimonies = courtTestimonies;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 97 * hash + Objects.hashCode(this.username);
    hash = 97 * hash + Objects.hashCode(this.workdayDate);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Workday other = (Workday) obj;
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.workdayDate, other.workdayDate)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Workday{" + "id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", username=" + username + ", workdayDate=" + workdayDate + ", workEntries=" + workEntries + ", courtTestimonies=" + courtTestimonies + '}';
  }
}
