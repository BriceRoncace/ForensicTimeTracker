<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:page>
  <jsp:attribute name="body">

    <div class="row">
      <div class="col">
        <h2 class="border-bottom d-inline-block">Users</h2>
        <span class="badge badge-secondary" style="vertical-align: super">${users.size()}</span>

        <div class="float-right">
          <button type="button" id="addUser" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#userModal">
            Add User
          </button>
        </div>
      </div>
    </div>

    <table class="table table-sm table-hover">
      <tr>
        <th>First Name</th>
        <th>Middle Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Username</th>
        <th>Role</th>
      </tr>
      <tbody>
        <c:forEach var="u" items="${users}">
          <tr class="pointer" data-modal-edit data-id="${u.id}" data-first="${u.firstName}" data-middle="${u.middleName}" data-last="${u.lastName}" data-email="${u.email}" data-username="${u.username}" data-user-role="${u.userRole}">
            <td>${u.firstName}</td>
            <td>${u.middleName}</td>
            <td>${u.lastName}</td>
            <td>${u.email}</td>
            <td>${u.username}</td>
            <td>${u.userRole}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <div class="modal fade" id="userModal" data-new-selector="#addUser" data-edit-selector="[data-modal-edit]" data-reset-on-hide="true" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
          <div class="modal-header bg-primary">
            <h5 class="modal-title text-white">Save User</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true" class="text-muted">&times;</span>
            </button>
          </div>

          <form action="<c:url value="/admin/user/save"/>" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <input type="hidden" data-value="id" id="id" name="id"/>

            <div class="modal-body">
              <div class="form-row">
                <div class="form-group col-5">
                  <label for="first" class="required">First Name</label>
                  <input type="text" class="form-control" data-value="first" id="first" name="firstName">
                </div>
                <div class="form-group col-2">
                  <label for="middle">Middle</label>
                  <input type="text" class="form-control" data-value="middle" id="middle" name="middleName">
                </div>
                <div class="form-group col-5">
                  <label for="last" class="required">Last</label>
                  <input type="text" class="form-control" data-value="last" id="last" name="lastName">
                </div>
              </div>

              <div class="form-row">
                <div class="form-group col-12">
                  <label for="first">Email</label>
                  <input type="text" class="form-control" data-value="email" id="email" name="email">
                </div>
              </div>

              <div class="form-row">
                <div class="form-group col-6">
                  <label for="first" class="required">Username</label>
                  <input type="text" class="form-control" data-value="username" id="username" name="username">
                </div>
                <div class="form-group col-6">
                  <label for="first">Password</label>
                  <input type="password" class="form-control" id="password" name="plaintextPassword">
                </div>
              </div>

              <div class="form-row">
                <div class="form-group col-12">
                  <label for="first" class="required">Role</label>
                  <select id="userRole" name="userRole" data-value="userRole" class="form-control">
                    <option value="USER">User</option>
                    <option value="ADMIN">Admin</option>
                  </select>
                </div>
              </div>

            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger mr-auto" data-submit="<c:url value="/admin/user/delete"/>">Delete</button>
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
              <button type="submit" class="btn btn-primary">Save</button>
            </div>
          </form>
        </div>
      </div>
    </div>

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