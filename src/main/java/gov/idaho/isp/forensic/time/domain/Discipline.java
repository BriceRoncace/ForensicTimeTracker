package gov.idaho.isp.forensic.time.domain;

public enum Discipline {
  ALCOHOL_VOLATILES("Alcohol / Volatiles"),
  BREATH_ALCOHOL("Breath Alcohol"),
  CONTROLLED_SUBSTANCES("Controlled Substances"),
  CRIME_SCENE("Crime Scene"),
  DNA_BIOLOGY("DNA Biology"),
  DNA_DATABASE("DNA Database"),
  EVIDENCE("Evidence"),
  FIRE_EVIDENCE("Fire Evidence"),
  FIREARMS("Firearms / Toolmarks"),
  IMPRESSIONS("Footwear & Impressions"),
  LATENTS("Latents"),
  MANAGEMENT("Management"),
  TOXICOLOGY("Toxicology");

  private final String label;

  private Discipline(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
