<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:page>
  <jsp:attribute name="body">

    <form action="<c:url value="/changePassword"/>" method="POST">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <input type="hidden" name="id" value="${user.id}"/>

      <div class="row">
        <div class="col">
          <h2 class="border-bottom d-inline-block">Change Password</h2>
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-5">
          <label class="required">Current Password</label>
          <input type="password" class="form-control" name="currentPassword">
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-5">
          <label class="required">New Password</label>
          <input type="password" class="form-control" name="password1">
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-5">
          <label class="required">Verify New Password</label>
          <input type="password" class="form-control" name="password2">
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-5">
          <div class="float-right">
            <a href="<c:url value="/"/>" class="btn btn-secondary" >Cancel</a>
            <button type="submit" class="btn btn-primary">Change</button>
          </div>
        </div>
      </div>
    </form>

  </jsp:attribute>
  <jsp:attribute name="javascript">
    <script type="text/javascript" src="<c:url value="/assets/js/cjis/page/cjis.page.users.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/assets/js/jquery.cjis.modal-form-0.0.1.js"/>"></script>
    <script type="text/javascript">
      $(function() {
        cjis.page.users.init();
      });
    </script>
  </jsp:attribute>
</t:page>