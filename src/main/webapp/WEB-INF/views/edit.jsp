<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="timePattern" value="^[0-9]{0,2}(\.(0|00|167|25|333|5|50|667|75|833))?$"/>
<c:set var="timeMaxLength" value="6"/>

<c:set var="caseNumberPattern" value="^([C|M|P]{1}[0-9]{4}-[0-9]{4};?\s*)*$"/>
<c:set var="caseNumberMaxLength" value="255"/>

<t:page>
  <jsp:attribute name="nav">
    <button type="button" class="btn btn-sm btn-secondary" onclick="$(window).off('beforeunload'); $('#workdayForm').submit();"><i class="fas fa-save"></i> Save</button>
  </jsp:attribute>
  <jsp:attribute name="body">

    <form id="workdayForm" action="<c:url value="/save"/>" method="POST" novalidate>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="username" value="${workday.username}"/>
      <input type="hidden" name="workdayDate" value="<spring:eval expression='workday.workdayDate'/>"/>

      <div class="row mb-2">
        <div class="col-6">
          <span class="display-7">
            <b><spring:eval expression="workday.workdayDate"/> Time Entry</b>
            <small class="text-secondary">for ${workday.displayName}</small>
          </span>
        </div>

        <div class="col-4 form-inline">
          <label class="mr-2 text-primary font-weight-bold required">Lab</label>
          <div class="form-group">
            <select id="lab" name="lab" class="form-control form-control-sm" data-cookie-ize="lab" required>
              <option></option>
              <c:forEach var="lab" items="${labs}">
                <option value="${lab}" ${workday.lab == lab ? 'selected' : ''}>${lab.label}</option>
              </c:forEach>
            </select>
            <div class="invalid-tooltip">Required</div>
          </div>
        </div>

        <div class="col-2 d-flex align-items-end justify-content-end pb-1">
          <c:if test="${workday.id == null}">
            <a href="<c:url value="/copyPrevious?username=${workday.username}&workdayDate=${workday.workdayDate}"/>" class="btn btn-sm btn-outline-primary grey-border mr-1">Copy Previous</a>
          </c:if>
          <button id="selectCategories" type="button" class="btn btn-sm btn-outline-primary grey-border btn-bold-icon"><i class="fas fa-cog"></i></button>
        </div>
      </div>

      <div class="row">
        <div class="col-8 small font-weight-bold">Hours for Week</div>
        <div class="offset-2 col-2 small font-weight-bold">Hours for this Entry</div>
      </div>

      <div class="row">
        <div class="col-8">
          <table class=" table table-sm table-centered table-tertiary-grid">
            <thead class="bg-header">
              <tr>
                <th class="py-0 ${workday.getDayOfWeek() == 'SUNDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('SUNDAY').workdayDate}"/>">Sun  ${shortDateFmt.format(workweek.getDay('SUNDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'MONDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('MONDAY').workdayDate}"/>">Mon ${shortDateFmt.format(workweek.getDay('MONDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'TUESDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('TUESDAY').workdayDate}"/>">Tue ${shortDateFmt.format(workweek.getDay('TUESDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'WEDNESDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('WEDNESDAY').workdayDate}"/>">Wed ${shortDateFmt.format(workweek.getDay('WEDNESDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'THURSDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('THURSDAY').workdayDate}"/>">Thu ${shortDateFmt.format(workweek.getDay('THURSDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'FRIDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('FRIDAY').workdayDate}"/>">Fri ${shortDateFmt.format(workweek.getDay('FRIDAY').workdayDate)}</a></th>
                <th class="py-0 ${workday.getDayOfWeek() == 'SATURDAY' ? 'bg-tertiary' : ''}" colspan="2"><a href="<c:url value="/load?username=${workday.username}&workdayDate=${workweek.getDay('SATURDAY').workdayDate}"/>">Sat ${shortDateFmt.format(workweek.getDay('SATURDAY').workdayDate)}</a></th>
                <th class="py-0 text-primary" colspan="3">Totals</th>
              </tr>
            </thead>
            <tbody class="bg-white">
              <tr>
                <c:forEach var="day" items="${workweek.week}">
                  <td class="py-1">${day.getNormalHoursTotal()}</td>
                  <td class="py-1">${day.getOvertimeHoursTotal()}</td>
                </c:forEach>
                <td class="py-1 text-secondary">${workweek.getWeekNormalTimeTotal()}</td>
                <td class="py-1 text-secondary">${workweek.getWeekOvertimeTotal()}</td>
                <td class="py-1 text-primary"><b>${workweek.getWeekTimeTotal()}</b></td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="offset-2 col-2">
          <table class="table table-sm table-centered table-tertiary-grid text-primary font-weight-bold">
            <thead class="bg-header">
              <tr>
                <th class="py-0">Time</th>
                <th class="py-0">O.T.</th>
              </tr>
            </thead>
            <tbody class="bg-white">
              <tr>
                <td class="py-1" data-sum="time">${workday.normalHoursTotal}</td>
                <td class="py-1" data-sum="overtime">${workday.overtimeHoursTotal}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="row">
        <div id="discipline-zone" class="col-12">
          <c:if test="${not empty workday.workEntries}">
            <c:forEach var="workEntry" items="${workday.workEntries}" varStatus="i">

              <div class="card mb-3">
                <div class="card-header">
                  <span class="card-title text-primary">
                    <span class="display-8">Work Entry</span>
                    <button type="button" class="close" data-remove="discipline" aria-label="Close">
                      <span aria-hidden="true" class="font1Point3">&times;</span>
                    </button>
                  </span>
                </div>

                <div class="card-body">
                  <div class="row d-flex justify-content-between">
                    <div class="col-6 d-flex align-items-end mb-3">
                      <span class="mr-2 mb-2 text-primary font-weight-bold required">Discipline</span>
                      <div class="w-100">
                        <select name="workEntries[${i.index}].discipline" value="${workEntry.discipline}" ${i.first ? 'data-cookie-ize="discipline"' : ''} class="form-control" required>
                          <option></option>
                          <c:forEach var="discipline" items="${disciplines}">
                            <option value="${discipline}" ${workEntry.discipline == discipline ? 'selected' : ''}>${discipline.label}</option>
                          </c:forEach>
                        </select>
                        <div class="invalid-tooltip">Required</div>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-12 d-none" data-hidden-entries="${i.index}">
                      <c:forEach var="entry" items="${workEntry.entries}" varStatus="y">
                        <div class="form-row mb-1" data-entry="${i.index}" data-category="${entry.category}" data-label="${entry.category.label}">
                          <div class="col text-truncate">
                            ${entry.category.label}
                            <c:if test="${entry.category.tip != ''}">
                              <i class="fas fa-info-circle small text-primary" data-toggle="popover" data-content="${entry.category.tip}"></i>
                            </c:if>
                          </div>

                          <div class="col-2 input-group">
                            <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                            <input type="text" name="workEntries[${i.index}].entries[${y.index}].hours.normal" data-addends="time" value="${entry.hours.normal}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}" />
                            <div class="invalid-tooltip">Invalid</div>
                          </div>

                          <div class="col-2 input-group">
                            <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                            <input type="text" name="workEntries[${i.index}].entries[${y.index}].hours.overtime" data-addends="overtime" value="${entry.hours.overtime}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                            <div class="invalid-tooltip">Invalid</div>
                          </div>

                        </div>
                      </c:forEach>
                    </div>
                    <div class="col">
                      <div class="card">
                        <div class="card-body" data-entry-column="${i.index}">
                          <div class="form-row text-primary font-weight-bold mb-1">
                            <div class="col">Category</div>
                            <div class="col-2 text-center">Time</div>
                            <div class="col-2 text-center">O.T.</div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="col">
                      <div class="card">
                        <div class="card-body" data-entry-column="${i.index}">
                          <div class="form-row text-primary font-weight-bold mb-1">
                            <div class="col">Category</div>
                            <div class="col-2 text-center">Time</div>
                            <div class="col-2 text-center">O.T.</div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>


            </c:forEach>
          </c:if>
        </div>
      </div>
      <div class="row">
        <div class="col-12">
          <button type="button" data-add-entry='{"name": "discipline", "index": ${workday.workEntries.size()}}' class="btn btn-sm btn-outline-primary mb-3">+ Add Work Entry</button>
        </div>
      </div>

      <div class="row">
        <div id="court-zone" class="col-12">
          <c:if test="${not empty workday.courtTestimonies}">
            <c:forEach var="court" items="${workday.courtTestimonies}" varStatus="i">

              <div class="card mb-3" data-court-index="${i.index}">
                <div class="card-header">
                  <span class="card-title text-primary">
                    <span class="display-8">Court Testimony</span>
                    <button type="button" class="close" data-remove="court" aria-label="Close">
                      <span aria-hidden="true" class="font1Point3">&times;</span>
                    </button>
                  </span>
                </div>

                <div class="card-body">
                  <div class="row">
                    <div class="col-6">
                      <div class="form-group">
                        <label class="control-label font-weight-bold text-primary required">Lab Case Number(s) <span class="small" data-toggle="popover" data-content="Try entering using the number buttons first then press the lab and year as needed. To enter more than one case number, separate numbers using a ; (semicolon) character."><i class="fas fa-question-circle"></i></span></label>
                        <input type="text" name="courtTestimonies[${i.index}].caseNumbersJoined" data-lab-case-number class="form-control" value="${workday.courtTestimonies[i.index].caseNumbersJoined}" required maxlength="${caseNumberMaxLength}" pattern="${caseNumberPattern}"/>
                        <div class="invalid-tooltip top-inherit">Required</div>
                      </div>
                    </div>
                    <div class="col-3">
                      <div class="form-group">
                        <label class="control-label font-weight-bold text-primary required">County</label>
                        <select class="form-control" name="courtTestimonies[${i.index}].county" required>
                          <option></option>
                          <c:forEach var="county" items="${counties}">
                            <option value="${county}" ${workday.courtTestimonies[i.index].county == county ? 'selected' : ''}>${county.label}</option>
                          </c:forEach>
                        </select>
                        <div class="invalid-tooltip top-inherit">Required</div>
                      </div>
                    </div>
                    <div class="col-3">
                      <div class="form-group">
                        <label class="control-label font-weight-bold text-primary required">Discipline</label>
                        <select id="discipline" name="courtTestimonies[${i.index}].discipline" class="form-control" required>
                          <option></option>
                          <c:forEach var="discipline" items="${disciplines}">
                            <option value="${discipline}" ${workday.courtTestimonies[i.index].discipline == discipline ? 'selected' : ''}>${discipline.label}</option>
                          </c:forEach>
                        </select>
                        <div class="invalid-tooltip top-inherit">Required</div>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col">
                      <div class="card">
                        <div class="card-body">
                          <div class="form-row text-primary font-weight-bold mb-1">
                            <div class="col">Category</div>
                            <div class="col-2 text-center">Time</div>
                            <div class="col-2 text-center">O.T.</div>
                          </div>

                          <c:forEach var="courtEntry" items="${workday.courtTestimonies[i.index].entries}" varStatus="y" step="2">
                            <div class="form-row mb-1">
                              <div class="col text-truncate">${courtEntry.category.label}</div>

                              <div class="col-2 input-group">
                                <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                                <input type="text" name="courtTestimonies[${i.index}].entries[${y.index}].hours.normal" data-addends="time" value="${courtEntry.hours.normal}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                                <div class="invalid-tooltip">Invalid</div>
                              </div>
                              <div class="col-2 input-group">
                                <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                                <input type="text" name="courtTestimonies[${i.index}].entries[${y.index}].hours.overtime" data-addends="overtime" value="${courtEntry.hours.overtime}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                                <div class="invalid-tooltip">Invalid</div>
                              </div>
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </div>
                    <div class="col">
                      <div class="card">
                        <div class="card-body">
                          <div class="form-row text-primary font-weight-bold mb-1">
                            <div class="col">Category</div>
                            <div class="col-2 text-center">Time</div>
                            <div class="col-2 text-center">O.T.</div>
                          </div>

                          <c:forEach var="courtEntry" items="${workday.courtTestimonies[i.index].entries}" varStatus="y" step="2" begin="1">
                            <div class="form-row mb-1">
                              <div class="col text-truncate">${courtEntry.category.label}</div>

                              <div class="col-2 input-group">
                                <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                                <input type="text" name="courtTestimonies[${i.index}].entries[${y.index}].hours.normal" data-addends="time" value="${courtEntry.hours.normal}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                                <div class="invalid-tooltip">Invalid</div>
                              </div>
                              <div class="col-2 input-group">
                                <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                                <input type="text" name="courtTestimonies[${i.index}].entries[${y.index}].hours.overtime" data-addends="overtime" value="${courtEntry.hours.overtime}" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                                <div class="invalid-tooltip">Invalid</div>
                              </div>
                            </div>
                          </c:forEach>
                        </div>
                      </div>
                    </div>

                  </div>
                </div>
              </div>
            </c:forEach>
          </c:if>
        </div>
      </div>

      <div class="row">  
        <div class="col-10">
          <button type="button" data-add-entry='{"name": "court", "index": ${workday.courtTestimonies.size()}}' class="btn btn-sm btn-outline-primary">+ Add Court Testimony</button>
        </div>
        <div class="col-2">
          <span class="small font-weight-bold">Hours for this Entry</span>
          <table class="table table-sm table-centered table-tertiary-grid text-primary font-weight-bold">
            <thead class="bg-header">
              <tr>
                <th class="py-0">Time</th>
                <th class="py-0">O.T.</th>
              </tr>
            </thead>
            <tbody class="bg-white">
              <tr>
                <td class="py-1" data-sum="time">${workday.normalHoursTotal}</td>
                <td class="py-1" data-sum="overtime">${workday.overtimeHoursTotal}</td>
              </tr>
            </tbody>
          </table>
          <button type="button" class="btn btn-sm btn-outline-primary float-right m0" onclick="$(window).off('beforeunload'); $('#workdayForm').submit();"><i class="fas fa-save"></i> Save</button>
        </div>
      </div>  

    </form>

    <div class="modal fade" id="catSelectModal" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header bg-primary">
            <h5 class="modal-title text-white">Category Selection</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true" class="text-muted">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="mb-5">
              <span>Select Categories needed for time entry below:</span>
              <div class="float-right">
                <button id="checkAllCategories" type="button" class="btn btn-outline-secondary btn-sm">Check All</button>
                <button id="uncheckAllCategories" type="button" class="btn btn-outline-secondary btn-sm">Un-Check All</button>
              </div>
            </div>
            <div class="row">
              <c:forEach var="cat" items="${workEntryCategories}">
                <div class="col-6">
                  <div class="custom-control custom-checkbox">
                    <input id="toggle-${cat}" type="checkbox" class="custom-control-input" value="${cat}">
                    <label class="custom-control-label" for="toggle-${cat}">${cat.label}</label>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
            <button id="updateCategories" type="button" class="btn btn-primary">OK</button>
          </div>
        </div>
      </div>
    </div>

    <div id="discipline-clone" class="d-none">
      <div class="card mb-3">
        <div class="card-header">
          <span class="card-title text-primary">
            <span class="display-8">Work Entry</span>
            <button type="button" class="close" data-remove="discipline" aria-label="Close">
              <span aria-hidden="true" class="font1Point3">&times;</span>
            </button>
          </span>
        </div>

        <div class="card-body">
          <div class="row d-flex justify-content-between">
            <div class="col-6 d-flex align-items-end mb-3">
              <span class="mr-2 mb-2 text-primary font-weight-bold required">Discipline</span>
              <div class="w-100">
                <select name="workEntries[{indexHere}].discipline" class="form-control" required>
                  <option></option>
                  <c:forEach var="discipline" items="${disciplines}">
                    <option value="${discipline}">${discipline.label}</option>
                  </c:forEach>
                </select>
                <div class="invalid-tooltip">Required</div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-12 d-none" data-hidden-entries="{indexHere}">
              <c:forEach var="category" items="${workEntryCategories}" varStatus="y">
                <div class="form-row mb-1" data-entry="{indexHere}" data-category="${category}">
                  <div class="col text-truncate">${category.label}</div>

                  <div class="col-2 input-group">
                    <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                    <input type="text" name="workEntries[{indexHere}].entries[${y.index}].hours.normal" data-addends="time" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                    <div class="invalid-tooltip" style="left:inherit">Invalid</div>
                  </div>

                  <div class="col-2 input-group">
                    <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                    <input type="text" name="workEntries[{indexHere}].entries[${y.index}].hours.overtime" data-addends="overtime" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                    <div class="invalid-tooltip">Invalid</div>
                  </div>

                </div>
              </c:forEach>
            </div>
            <div class="col">
              <div class="card">
                <div class="card-body" data-entry-column="{indexHere}">
                  <div class="form-row text-primary font-weight-bold mb-1">
                    <div class="col">Category</div>
                    <div class="col-2 text-center">Time</div>
                    <div class="col-2 text-center">O.T.</div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="card">
                <div class="card-body" data-entry-column="{indexHere}">
                  <div class="form-row text-primary font-weight-bold mb-1">
                    <div class="col">Category</div>
                    <div class="col-2 text-center">Time</div>
                    <div class="col-2 text-center">O.T.</div>
                  </div>
                </div>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div id="court-clone" class="d-none">
      <div class="card mb-3" data-court-index="{indexHere}">
        <div class="card-header">
          <span class="card-title text-primary">
            <span class="display-8">Court Testimony</span>
            <button type="button" class="close" data-remove="court" aria-label="Close">
              <span aria-hidden="true" class="font1Point3">&times;</span>
            </button>
          </span>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-6">
              <div class="form-group">
                <label class="control-label font-weight-bold text-primary required">Lab Case Number(s) <span class="small" data-toggle="popover" data-content="Try entering using the number buttons first then press the lab and year as needed. To enter more than one case number, separate numbers using a ; (semicolon) character."><i class="fas fa-question-circle"></i></span></label>
                <input type="text" name="courtTestimonies[{indexHere}].caseNumbersJoined" data-lab-case-number class="form-control" required maxlength="${caseNumberMaxLength}" pattern="${caseNumberPattern}"/>
                <div class="invalid-tooltip top-inherit">Required</div>
              </div>
            </div>
            <div class="col-3">
              <div class="form-group">
                <label class="control-label font-weight-bold text-primary required">County</label>
                <select class="form-control" name="courtTestimonies[{indexHere}].county" required>
                  <option></option>
                  <c:forEach var="county" items="${counties}">
                    <option value="${county}">${county.label}</option>
                  </c:forEach>
                </select>
                <div class="invalid-tooltip top-inherit">Required</div>
              </div>
            </div>
            <div class="col-3">
              <div class="form-group">
                <label class="control-label font-weight-bold text-primary required">Discipline</label>
                <select id="discipline" name="courtTestimonies[{indexHere}].discipline" class="form-control" required>
                  <option></option>
                  <c:forEach var="discipline" items="${disciplines}">
                    <option value="${discipline}">${discipline.label}</option>
                  </c:forEach>
                </select>
                <div class="invalid-tooltip top-inherit">Required</div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <div class="card">
                <div class="card-body">
                  <div class="form-row text-primary font-weight-bold mb-1">
                    <div class="col">Category</div>
                    <div class="col-2 text-center">Time</div>
                    <div class="col-2 text-center">O.T.</div>
                  </div>

                  <c:forEach var="courtCategory" items="${courtEntryCategories}" varStatus="y" step="2">
                    <div class="form-row mb-1">
                      <div class="col text-truncate">${courtCategory.label}</div>

                      <div class="col-2 input-group">
                        <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                        <input type="text" name="courtTestimonies[{indexHere}].entries[${y.index}].hours.normal" data-addends="time" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                        <div class="invalid-tooltip">Invalid</div>
                      </div>
                      <div class="col-2 input-group">
                        <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                        <input type="text" name="courtTestimonies[{indexHere}].entries[${y.index}].hours.overtime" data-addends="overtime" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                        <div class="invalid-tooltip">Invalid</div>
                      </div>
                    </div>
                  </c:forEach>

                </div>
              </div>
            </div>
            <div class="col">
              <div class="card">
                <div class="card-body">
                  <div class="form-row text-primary font-weight-bold mb-1">
                    <div class="col">Category</div>
                    <div class="col-2 text-center">Time</div>
                    <div class="col-2 text-center">O.T.</div>
                  </div>

                  <c:forEach var="courtCategory" items="${courtEntryCategories}" varStatus="y" step="2" begin="1">
                    <div class="form-row mb-1">
                      <div class="col text-truncate">${courtCategory.label}</div>

                      <div class="col-2 input-group">
                        <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                        <input type="text" name="courtTestimonies[{indexHere}].entries[${y.index}].hours.normal" data-addends="time" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                        <div class="invalid-tooltip">Invalid</div>
                      </div>
                      <div class="col-2 input-group">
                        <a tabindex="-1" class="input-group-prepend" role="button" data-hour-selector><span class="input-group-text px-1" title="calculator"><i class="fas fa-calculator text-tertiary"></i></span></a>
                        <input type="text" name="courtTestimonies[{indexHere}].entries[${y.index}].hours.overtime" data-addends="overtime" class="form-control form-control-sm" maxlength="${timeMaxLength}" pattern="${timePattern}"/>
                        <div class="invalid-tooltip">Invalid</div>
                      </div>
                    </div>
                  </c:forEach>

                </div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

  </jsp:attribute>
  <jsp:attribute name="javascript">
    <script type="text/javascript" src="<c:url value="/assets/js/jquery.cjis.cookie.ize.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/assets/js/jquery.cjis.labCaseNumberSelector.js"/>"></script>

    <script type="text/javascript" src="<c:url value="/assets/js/cjis/page/cjis.page.edit.js"/>"></script>
    <script type="text/javascript">
              $(function() {
                cjis.page.edit.settingsModal();
                cjis.page.edit.addRemoveEntries();
                cjis.page.edit.redirectOnCookie();
                cjis.page.edit.hourPicker();
                cjis.page.edit.cookieizeLabAndDiscipline();
                cjis.page.edit.calculateTotals();
                cjis.page.edit.confirmLeaveAfterChange();
                cjis.page.edit.labCaseNumber();
                cjis.page.edit.formValidation();
                cjis.page.edit.initPopovers();
              });
    </script>
  </jsp:attribute>
</t:page>
