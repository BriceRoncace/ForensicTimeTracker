<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:page>
  <jsp:attribute name="body">
    <div class="row d-flex justify-content-center">
      <div class="col-4">
        <c:if test="${not empty paramValues.error}">
          <div class="alert alert-warning alert-dismissible"><button type="button" class="close" data-dismiss="alert" title="close error(s)">&times;</button>
            Invalid username and password.
          </div>
        </c:if>
        <c:if test="${not empty paramValues.logout}">
          <div class="alert alert-secondary alert-dismissible"><button type="button" class="close" data-dismiss="alert" title="close message(s)">&times;</button>
            You have been logged out.
          </div>
        </c:if>
      </div>
    </div>

    <div class="row d-flex justify-content-center">
      <div class="col-4">
        <h2 class="border-bottom"><i class="fa fa-lock"></i> Login</h2>
      </div>
    </div>
    
    <div class="row d-flex justify-content-center">
      <div class="col-4">
        <form action="<c:url value="/login"/>" method="POST">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
          <p class="h4 mb-3">Please sign in</p>

          <div class="form-group">
            <label>Username</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="username" required autofocus>
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required>
          </div>

          <button class="btn btn-outline-primary float-right" type="submit">Sign in</button>
        </form>
      </div>
    </div>

  </jsp:attribute>
</t:page>
