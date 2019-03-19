package gov.idaho.isp.forensic.time.domain;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class WorkEntry implements Serializable {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull(message = "{discipline.null}")
  @Enumerated(EnumType.STRING)
  private Discipline discipline;

  @Valid
  @OneToMany(cascade = CascadeType.ALL)
  private List<Entry> entries = new ArrayList<>();

  public WorkEntry() {
    this.entries = Category.workEntryValues().stream().map(c -> new Entry(c)).collect(Collectors.toList());
  }

  public List<Hours> getAllHours() {
    return entries.stream().map(e -> e.getHours()).filter(Objects::nonNull).collect(Collectors.toList());
  }

  public boolean isEmpty() {
    return discipline == null
      && (entries.isEmpty() || (entries.stream().allMatch(e -> e.isEmpty())));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return "WorkEntry{" + "id=" + id + ", discipline=" + discipline + ", entries=" + entries + '}';
  }
}
