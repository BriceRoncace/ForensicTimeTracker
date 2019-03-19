<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:page>
  <jsp:attribute name="body">
    
    <c:choose>
      <c:when test="${status == 403}">
        <div class="alert alert-warning" role="alert">
          <h2 class="alert-heading border-bottom border-warning">Access Denied</h2>
          <p>You do not have the appropriate permissions to perform this action.</p>
        </div>
      </c:when>
      <c:otherwise>
        <div class="alert alert-warning" role="alert">
          <h2 class="alert-heading border-bottom border-warning d-flex justify-content-between">
            <span>Invalid Request</span>
            <span>${timestamp}</span>
          </h2>
          <p>Please use the back button and try again.</p>
        </div>
        
        <div class="card">
          <h5 class="card-header d-flex justify-content-between">
            <span>${status} - ${error}</span>
          </h5>
          <div class="card-body">
            <c:if test="${not empty exception}">
              <h5 class="card-title">Exception: ${exception}</h5>
            </c:if>
            <c:if test="${not empty message}">
              <h5 class="card-title">Message: ${message}</h5>
            </c:if>
            <c:if test="${not empty trace}">
            <a href="javascript://nop" onclick="$('#trace').removeClass('d-none'); $(this).remove();">View Details</a>
            <pre id="trace" class="d-none">${trace}</pre>
            </c:if>
          </div>
        </div>
      </c:otherwise>
    </c:choose>

  </jsp:attribute>
</t:page>