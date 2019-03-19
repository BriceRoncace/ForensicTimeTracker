package gov.idaho.isp.forensic.time.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.apache.commons.lang3.StringUtils;

@Entity
public class CourtTestimony implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @ElementCollection
  @NotEmpty(message="{caseNumbers.empty}")
  private List<@Pattern(regexp="[C|M|P]20[0-9]{2}-[0-9]{4}", message="{caseNumber.invalid}")String> caseNumbers = new ArrayList<>();

  @NotNull(message = "{county.null}")
  @Enumerated(EnumType.STRING)
  private County county;

  @NotNull(message = "{discipline.null}")
  @Enumerated(EnumType.STRING)
  private Discipline discipline;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Entry> entries = new ArrayList<>();

  public CourtTestimony() {
    this.entries = Category.courtEntryValues().stream().map(c -> new Entry(c)).collect(Collectors.toList());
  }

  public boolean isEmpty() {
    return county == null
      && discipline == null
      && (caseNumbers.isEmpty() || (caseNumbers.stream().allMatch(e -> e.isEmpty())))
      && (entries.isEmpty() || (entries.stream().allMatch(e -> e.isEmpty())));
  }

  public List<Hours> getAllHours() {
    return entries.stream().map(e -> e.getHours()).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCaseNumbersJoined() {
    return String.join("; ", caseNumbers);
  }

  public void setCaseNumbersJoined(String caseNumbers) {
    if (caseNumbers != null) {
      this.caseNumbers = Arrays.stream(caseNumbers.split(";")).filter(StringUtils::isNotBlank).map(String::trim).map(String::toUpperCase).collect(Collectors.toList());
    }
  }

  public List<String> getCaseNumbers() {
    return caseNumbers;
  }

  public void setCaseNumbers(List<String> caseNumbers) {
    this.caseNumbers = caseNumbers;
  }

  public County getCounty() {
    return county;
  }

  public void setCounty(County county) {
    this.county = county;
  }

  public Discipline getDiscipline() {
    return discipline;
  }

  public void setDiscipline(Discipline discipline) {
    this.discipline = discipline;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public void setEntries(List<Entry> entries) {
    this.entries = entries;
  }

  @Override
  public String toString() {
    return "CourtTestimony{" + "id=" + id + ", caseNumbers=" + caseNumbers + ", county=" + county + ", discipline=" + discipline + ", entries=" + entries + '}';
  }
}
