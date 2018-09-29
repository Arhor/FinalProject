<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 24.09.2018
  Time: 0:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${locale}" />
<fmt:setBundle basename="resources.pagecontent" />
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>
        <fmt:message key="label.faculties" />
    </title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css">
    <!-- CSS Files -->
    <link href="${pageContext.request.contextPath}/assets/css/material-dashboard.css?v=2.1.0" rel="stylesheet" />
</head>

<body class="">
<div class="wrapper ">
    <div class="sidebar" data-color="purple" data-background-color="white">
        <!--
          Tip 1: You can change the color of the sidebar using: data-color="purple | azure | green | orange | danger"

          Tip 2: you can also add an image using data-image tag
      -->
        <div class="logo">
            <span class="simple-text logo-normal">
                <fmt:message key="label.title" />
            </span>
        </div>
        <div class="sidebar-wrapper">
            <ul class="nav">
                <li class="nav-item active">
                    <a class="nav-link" href="/controller?command=home">
                        <i class="material-icons">home</i>
                        <p><fmt:message key="label.homepage" /></p>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="/controller?command=profile">
                        <i class="material-icons">person</i>
                        <p><fmt:message key="label.profile" /></p>
                    </a>
                </li>
                <li class="nav-item active  ">
                    <a class="nav-link" href="/controller?command=show_faculties">
                        <i class="material-icons">domain</i>
                        <p><fmt:message key="label.faculties" /></p>
                    </a>
                </li>
                <li class="nav-item active  ">
                    <a class="nav-link" href="/controller?command=show_users">
                        <i class="material-icons">people</i>
                        <p><fmt:message key="label.admin.users" /></p>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="/controller?command=logout">
                        <i class="material-icons">exit_to_app</i>
                        <p><fmt:message key="label.logout" /></p>
                    </a>
                </li>
                <!-- your sidebar here -->
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <c:import url="/jsp/modules/navbar_main.jspf" />
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h4 class="card-title"><fmt:message key="label.admin.users" /></h4>
                                <p class="card-category"><fmt:message key="label.admin.users.table.description" /></p>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead class="text-primary">
                                        <tr>
                                            <th><fmt:message key="label.faculties.table.id" /></th>
                                            <th><fmt:message key="label.email" /></th>
                                            <th><fmt:message key="label.name.first" /></th>
                                            <th><fmt:message key="label.name.last" /></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${users}" var="currUser">
                                            <tr>
                                                <td>${currUser.id}</td>
                                                <td>${currUser.email}</td>
                                                <td>${currUser.firstName}</td>
                                                <td>${currUser.lastName}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                <nav aria-label="Page navigation example">
                                    <ul class="pagination justify-content-center">
                                        <c:choose>
                                            <c:when test="${sessionScope.pageNum <= 0}">
                                                <li class="page-item disabled">
                                                    <a class="page-link" href="/controller?command=show_faculties_prev" tabindex="-1">Previous</a>
                                                </li>
                                            </c:when>
                                            <c:when test="${sessionScope.pageNum > 0}">
                                                <li class="page-item">
                                                    <a class="page-link" href="/controller?command=show_faculties_prev">Previous</a>
                                                </li>
                                            </c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${sessionScope.pageNum >= sessionScope.pageMax}">
                                                <li class="page-item disabled">
                                                    <a class="page-link" href="/controller?command=show_faculties_next" tabindex="-1">Next</a>
                                                </li>
                                            </c:when>
                                            <c:when test="${sessionScope.pageNum < sessionScope.pageMax}">
                                                <li class="page-item">
                                                    <a class="page-link" href="/controller?command=show_faculties_next">Next</a>
                                                </li>
                                            </c:when>
                                        </c:choose>
                                    </ul>
                                </nav>
                                <%--<div class="row">--%>
                                    <%--<div class="col-4 mr-auto ml-auto">--%>
                                        <%--<c:if test="${requestScope.pageNum != 0}">--%>
                                            <%--<a href="#" class="btn btn-primary btn-sm float-left">prev</a>--%>
                                        <%--</c:if>--%>
                                        <%--<c:if test="${requestScope.pageNum != requestScope.pageMax}">--%>
                                            <%--<a href="#" class="btn btn-primary btn-sm float-right">next</a>--%>
                                        <%--</c:if>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <footer class="footer">
            <div class="container-fluid">
                <nav class="float-left">
                    <ul>
                        <li>
                            <%--<a href="https://www.creative-tim.com">--%>
                            <%--Creative Tim--%>
                            <%--</a>--%>
                        </li>
                    </ul>
                </nav>
                <c:import url="/jsp/modules/copyright.jspf" />
            </div>
        </footer>
    </div>
</div>
<!--   Core JS Files   -->
<script src="assets/js/core/jquery.min.js" type="text/javascript"></script>
<script src="assets/js/core/popper.min.js" type="text/javascript"></script>
<script src="assets/js/core/bootstrap-material-design.min.js" type="text/javascript"></script>
<script src="assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
<!-- Chartist JS -->
<script src="assets/js/plugins/chartist.min.js"></script>
<!--  Notifications Plugin    -->
<script src="assets/js/plugins/bootstrap-notify.js"></script>
<!-- Control Center for Material Dashboard: parallax effects, scripts for the example pages etc -->
<script src="assets/js/material-dashboard.min.js?v=2.1.0" type="text/javascript"></script>
</body>

</html>
