package gov.idaho.isp.forensic.time.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
  // WorkEntry Categories
  ADMINISTRATIVE("Administrative / Clerical Duties", "Office work, documentation, and administrative tasks (i.e. emails, case correspondence, Qualtrax, journal reviews, trial prep, etc)", WorkEntry.class),
  BREAK("Authorized Break", "", WorkEntry.class),
  CASE_WORK("Case Analysis / Work", "", WorkEntry.class),
  CODIS("CODIS Administrative Work", "", WorkEntry.class),
  MAINTENANCE("Equipment Maintenance", "Maintenance performed outside of routine quality assurance requirements (i.e. additional maintenance to correct/prevent potential issues, IT troubleshooting, etc)", WorkEntry.class),
  EVIDENCE("Evidence Tracking / Transfer", "Activities associated with evidence handling (i.e. agency submissions, checking evidence in/out, evidence return to agencies, etc)", WorkEntry.class),
  ILIMS("ILIMS Development and Administration", "", WorkEntry.class),
  INSTRUMENT_CALIBRATION("Instrument Calibration", "Calibrations performed outside of routine quality assurance requirements (i.e. instrument calibration for agency use)", WorkEntry.class),
  IT("IT Implementation or Troubleshooting", "", WorkEntry.class),
  LEAVE_TIME("Leave Time", "Any time off to include vacation, comp time, and sick leave.", WorkEntry.class),
  EXTERNAL_MEETING("Meeting External", "Meetings that take place off ISP campus or with external individuals (i.e. pretrial conferences, legislative meetings, etc)", WorkEntry.class),
  INTERNAL_MEETING("Meeting Internal", "Meetings that take place on ISP campus or with internal individuals (i.e. lab meetings, production board meetings, etc)", WorkEntry.class),
  METHOD_DEVELOPMENT("Method Development", "Activities and documentation performed to develop and bring online new/improved methods (i.e. planning/research, validation performance, write-ups, etc)", WorkEntry.class),
  SUPPLIES("Ordering / Receiving Supplies", "", WorkEntry.class),
  PUBLIC_RELATIONS("Public Relations", "Activities performed to inform/update the public in regards to ISP Forensics operations (i.e. student presentations, booths, powerpoints provided, tours, news interviews, etc)", WorkEntry.class),
  QUALITY("Quality Assurance", "Activities performed to fulfill QA requirements (i.e. audits, instrument maintenance, proficiency testing, etc)", WorkEntry.class),
  SUPERVISORY("Supervisory Tasks", "Performance evaluations, employee counseling, meetings on personnel issues, documenting performance issues", WorkEntry.class),
  TECH_REVIEW("Technical Review", "", WorkEntry.class),
  TECH_VERIFICATION("Technical Verification", "", WorkEntry.class),
  TECH_WORK("Technician Work", "", WorkEntry.class),
  TRAINING_GIVEN("Training Given", "Training provided either internal or external to the laboratory (i.e. training other analysts, training other agencies, etc)", WorkEntry.class),
  TRAINING_RECEIVED("Training Received", "Training received either internal or external to the laboratory (i.e. training conferences, educational courses, activities related to discipline training plans, etc)", WorkEntry.class),
  TRAVEL_GENERAL("Travel (General)", "Any paid travel time NOT associated with court testimony or instrument calibration (i.e. training, crime scene, etc)", WorkEntry.class),
  TRAVEL_FOR_INSTRUMENT_CALIBRATION("Travel for Instrument Calibration", "Any paid travel time associated instrument calibration", WorkEntry.class),

  // CourtTestimony Categories
  COURT_PREPARATION("Court Preparation (General)", "", CourtTestimony.class),
  PREPARE_AFFIDAVITS("Preparing Affidavits / ICR16 Disclosures", "", CourtTestimony.class),
  PREPARE_DISCOVERY("Preparing Discovery Documents and Materials", "", CourtTestimony.class),
  TRAVEL("Court Travel", "", CourtTestimony.class),
  WAITING("Court Waiting", "", CourtTestimony.class),
  TESTIFYING("Court Testifying", "", CourtTestimony.class),
  MONITORING("Court Monitoring", "", CourtTestimony.class);

  private final String label;
  private final String tip;
  private final Class forClass;

  private Category(String label, String tip, Class forClass) {
    this.label = label;
    this.tip = tip;
    this.forClass = forClass;
  }

  public String getLabel() {
    return label;
  }

  public String getTip() {
    return tip;
  }

  public boolean isFor(Class clazz) {
    return forClass == clazz;
  }

  public static List<Category> workEntryValues() {
    return Arrays.stream(Category.values()).filter(c -> c.isFor(WorkEntry.class)).collect(Collectors.toList());
  }

  public static List<Category> courtEntryValues() {
    return Arrays.stream(Category.values()).filter(c -> c.isFor(CourtTestimony.class)).collect(Collectors.toList());
  }
}
