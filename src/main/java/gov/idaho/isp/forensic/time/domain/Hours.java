package gov.idaho.isp.forensic.time.domain;

import gov.idaho.isp.forensic.time.domain.persistence.validation.AllowedFractions;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class Hours implements Serializable {

  @AllowedFractions({".0", ".167", ".25", ".333", ".5", ".667", ".75", ".833"})
  private BigDecimal normal;

  @AllowedFractions({".0", ".167", ".25", ".333", ".5", ".667", ".75", ".833"})
  private BigDecimal overtime;

  public boolean isEmpty() {
    return (normal == BigDecimal.ZERO || normal == null)
      && (overtime == BigDecimal.ZERO || overtime == null);
  }

  public BigDecimal getNormal() {
    return normal != null ? normal.stripTrailingZeros() : null;
  }

  public void setNormal(BigDecimal normal) {
    this.normal = normal;
  }

  public BigDecimal getOvertime() {
    return overtime != null ? overtime.stripTrailingZeros() : null;
  }

  public void setOvertime(BigDecimal overtime) {
    this.overtime = overtime;
  }

  @Override
  public String toString() {
    return "Hours{" + "normal=" + normal + ", overtime=" + overtime + '}';
  }
}
