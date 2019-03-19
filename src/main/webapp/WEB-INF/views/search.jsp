<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:page>
  <jsp:attribute name="body">

    <div class="row">
      <div class="col">
        <h2 class="border-bottom">Search</h2>
      </div>
    </div>
    
    <form action="<c:url value="/admin/search"/>" method="GET">
      <input type="hidden" name="submitted" value="true"/>

      <div class="form-row">
        <div class="form-group col">
          <label>First Name</label>
          <input class="form-control" type="text" name="firstName" value="${spec.firstName}"/>
        </div>
        <div class="form-group col">
          <label>Last Name</label>
          <input class="form-control" type="text" name="lastName" value="${spec.lastName}"/>
        </div>
        <div class="form-group col-4">
          <label>Date</label>
          <t:criteria-date name="workingDate" value="${spec.workingDate}" />
        </div>
        <div class="form-group col">
          <label>Lab Case Number</label>
          <input class="form-control" type="text" name="caseNumber" value="${spec.caseNumber}"/>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col">
          <label>Lab</label>
          <select class="form-control" name="labs" multiple>
            <c:forEach var="lab" items="${labs}">
              <option value="${lab}" ${spec.labs.contains(lab) ? 'selected' : ''}>${lab.label}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group col">
          <label>Discipline</label>
          <select class="form-control" name="disciplines" multiple>
            <c:forEach var="discipline" items="${disciplines}">
              <option value="${discipline}" ${spec.disciplines.contains(discipline) ? 'selected' : ''}>${discipline.label}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group col">
          <label>County</label>
          <select class="form-control" name="counties" multiple>
            <c:forEach var="county" items="${counties}">
              <option value="${county}" ${spec.counties.contains(county) ? 'selected' : ''}>${county.label}</option>
            </c:forEach>
          </select>
        </div>
        <div class="form-group col">
          <label>Categories</label>
          <select class="form-control" name="categories" multiple>
            <c:forEach var="category" items="${categories}">
              <option value="${category}" ${spec.categories.contains(category) ? 'selected' : ''}>${category.label}</option>
            </c:forEach>
          </select>
        </div>
      </div>
      <div class="form-row mb-2">
        <div class="col d-flex justify-content-end">
          <button id="exportSearch" type="button" class="btn btn-outline-primary" ${not spec.submitted || page.totalElements == 0 ? 'disabled' : ''}>Export</button>
          <button type="submit" class="btn btn-outline-primary ml-2">Search</button>
        </div>
      </div>
        
      <div class="row mb-2" data-page-number="${page.number}" data-number-of-elements="${page.numberOfElements}" data-total-elements="${page.totalElements}" data-total-pages="${page.totalPages}"></div>
      <div class="row ${not spec.submitted || page.totalElements == 0 ? 'd-none' : ''}">
        <div class="col">
          <table class="table table-sm table-hover" data-sort="${page.sort}">
            <thead>
              <tr>
                <th data-sort-on="firstName">First Name</th>
                <th data-sort-on="lastName">Last Name</th>
                <th data-sort-on="workdayDate">Date</th>
                <th>Total Hours</th>
                <th>Total Overtime</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="workday" items="${page.content}">
                <tr data-admin-load="<c:url value="/load"/>?username=${workday.username}&workdayDate=${workday.workdayDate}" class="pointer">
                  <td>${workday.firstName}</td>
                  <td>${workday.lastName}</td>
                  <td><spring:eval expression='workday.workdayDate'/></td>
                  <td>${workday.normalHoursTotal}</td>
                  <td>${workday.overtimeHoursTotal}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
      <div class="row mb-2 ${not spec.submitted || page.totalElements == 0 ? 'd-none' : ''}" data-page-number="${page.number}" data-number-of-elements="${page.numberOfElements}" data-total-elements="${page.totalElements}" data-total-pages="${page.totalPages}"></div>

    </form>
    
  </jsp:attribute>
  <jsp:attribute name="javascript">
    <script type="text/javascript" src="<c:url value="/assets/js/jquery.cjis.pager-1.0.0.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/assets/js/jquery.cjis.sorter-1.0.0.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/assets/js/cjis/page/cjis.page.search.js"/>"></script>
    <script type="text/javascript">
      $(function() {
        cjis.page.search.tableRowClick();
        cjis.page.search.exportClick();
        cjis.page.search.cjisPager();
        cjis.page.search.cjisSorter();
      });
    </script>
  </jsp:attribute>
</t:page>