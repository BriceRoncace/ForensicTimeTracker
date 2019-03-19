<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:page>
  <jsp:attribute name="head">
    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/fullcalendar/fullcalendar.min.css"/>"/>
  </jsp:attribute>
  <jsp:attribute name="nav">
    <c:choose>
      <c:when test="${user.admin}">
        <select id="userChooser" class="form-control form-control-sm ml-1">
          <option value="${user.username}" selected>${user.displayName}</option>
          <c:forEach var="u" items="${users}">
            <c:if test="${u.username != user.username}">
              <option value="${u.username}">${u.displayName}</option>
            </c:if>
          </c:forEach>
        </select>
        <a href="<c:url value="/admin/search"/>" class="btn btn-sm btn-secondary ml-1">Search</a>
        <a href="<c:url value="/admin/users"/>" class="btn btn-sm btn-secondary ml-1">Manage Users</a>
      </c:when>
      <c:otherwise>
        <input id="userChooser" type="hidden" value="${user.username}"/>
      </c:otherwise>
    </c:choose>
        
    <c:if test="${not empty user && user.id != null}">    
      <a href="<c:url value="/changePassword?id=${user.id}"/>" class="btn btn-sm btn-secondary ml-1">Change Password</a>
    </c:if>
  </jsp:attribute>
  <jsp:attribute name="body">
    <div class="row">
      <div id="calendar" class="col-10" title="Click on a day to enter time."></div>
      <div class="col-2">
        <h2 class="border-bottom">&nbsp;</h2>
        <table class="index-totals">
          <thead>
            <tr>
              <th>Totals</th>
            </tr>
          </thead>
          <tbody id="weekTotals"></tbody>
        </table>
      </div>
    </div>

  </jsp:attribute>
  <jsp:attribute name="javascript">
    <script type="text/javascript" src="<c:url value="/webjars/fullcalendar/fullcalendar.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/assets/js/cjis/page/cjis.page.index.js"/>"></script>
    <script>
      $(function() {
        cjis.page.index.initFullCalendar();
      });
    </script>
  </jsp:attribute>
</t:page>
