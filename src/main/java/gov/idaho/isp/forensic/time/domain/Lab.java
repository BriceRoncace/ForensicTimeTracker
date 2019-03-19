package gov.idaho.isp.forensic.time.domain;

public enum Lab {
  LAB1("Lab 1"),
  LAB2("Lab 2"),
  HQ("Headquarters");

  private final String label;

  private Lab(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
