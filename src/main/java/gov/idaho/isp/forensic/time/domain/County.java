package gov.idaho.isp.forensic.time.domain;

public enum County {
ADA("Ada"),
ADAMS("Adams"),
BANNOCK("Bannock"),
BEAR_LAKE("Bear Lake"),
BENEWAH("Benewah"),
BINGHAM("Bingham"),
BLAINE("Blaine"),
BOISE("Boise"),
BONNER("Bonner"),
BONNEVILLE("Bonneville"),
BOUNDARY("Boundary"),
BUTTE("Butte"),
CAMAS("Camas"),
CANYON("Canyon"),
CARIBOU("Caribou"),
CASSIA("Cassia"),
CLARK("Clark"),
CLEARWATER("Clearwater"),
CUSTER("Custer"),
ELMORE("Elmore"),
FRANKLIN("Franklin"),
FREMONT("Fremont"),
GEM("Gem"),
GOODING("Gooding"),
IDAHO("Idaho"),
JEFFERSON("Jefferson"),
JEROME("Jerome"),
KOOTENAI("Kootenai"),
LATAH("Latah"),
LEMHI("Lemhi"),
LEWIS("Lewis"),
LINCOLN("Lincoln"),
MADISON("Madison"),
MINIDOKA("Minidoka"),
NEZ_PERCE("Nez Perce"),
ONEIDA("Oneida"),
OWYHEE("Owyhee"),
PAYETTE("Payette"),
POWER("Power"),
SHOSHONE("Shoshone"),
TETON("Teton"),
TWIN_FALLS("Twin Falls"),
VALLEY("Valley"),
WASHINGTON("Washington");

private final String label;

  private County (String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}