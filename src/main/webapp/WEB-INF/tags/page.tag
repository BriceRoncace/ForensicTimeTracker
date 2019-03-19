<%@tag trimDirectiveWhitespaces="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@tag description="Page Layout" pageEncoding="UTF-8"%>
<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<%@attribute name="nav" fragment="true" %>
<%@attribute name="body" fragment="true" %>
<%@attribute name="javascript" fragment="true" %>

<!DOCTYPE html>
<html>
  <head>
    <!-- current page: ${pageContext.request.requestURI} -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title><c:choose><c:when test="${title == null}">Forensic Time Tracker</c:when><c:otherwise><jsp:invoke fragment="title" /></c:otherwise></c:choose></title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/bootstrap/css/bootstrap.min.css"/>"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootswatch/4.1.1/pulse/bootstrap.min.css" integrity="sha384-w1P8UfRjdTYLTgdq8Y19zjdV25oYPMGNzwtm3tIyZ/gK8JPu+8ZW9OVkKhnBuydY" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href='<c:url value="/assets/css/style.css"/>' />

    <jsp:invoke fragment="head" />

  </head>
  <body style="padding-top: 62px;">
    <div class="container">

      <nav class="navbar fixed-top navbar-dark bg-primary py-2">
        <a class="navbar-brand display-8" href="<c:url value="/"/>">
          <i class="fas fa-calendar-alt"></i> Forensic Time Tracker
          <c:if test="${activeProfile != 'intranet'}">
            <img style="margin-top:-17px" src="<c:url value="/assets/images/beta-stamp-sm.png"/>"/>
          </c:if>
        </a>

        <div class="ml-auto form-inline">
          <jsp:invoke fragment="nav" />
        </div>
        
        <c:if test="${not empty user}">
          <form class="form-inline ml-2" action="<c:url value="/logout"/>" method="POST">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <button type="submit" class="btn btn-sm btn-secondary"><i class="fas fa-sign-out-alt"></i> Logout</button>
          </form>
        </c:if>

      </nav>
          
      <%@include file="includes/messages.jspf" %>

      <jsp:invoke fragment="body" />

    </div>

    <script type="text/javascript" src="<c:url value="/webjars/jquery/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/webjars/bootstrap/js/bootstrap.bundle.min.js"/>"></script>
    <script type="text/javascript" src='<c:url value="/webjars/jquery-cookie/jquery.cookie.js"/>'></script>
    <script type="text/javascript" src="<c:url value="/webjars/momentjs/min/moment.min.js"/>"></script>
    
    <script type="text/javascript" src="<c:url value="/assets/js/cjis/cjis.js"/>"></script>
    <jsp:invoke fragment="javascript" />
  </body>
</html>
