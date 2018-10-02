<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 16.09.2018
  Time: 2:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="resources.pagecontent" />
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/favicon.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>
        <fmt:message key="label.title" />
    </title>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
    <!--     Fonts and icons     -->
    <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css">
    <!-- CSS Files -->
    <link href="${pageContext.request.contextPath}/assets/css/material-dashboard.css?v=2.1.0" rel="stylesheet" />
</head>

<body class="">

<!-- Modal -->
<%@ include file="/jsp/modules/SessionExpiredModal.jspf"%>

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
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=home">
                        <i class="material-icons">home</i>
                        <p><fmt:message key="label.homepage" /></p>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=profile">
                        <i class="material-icons">person</i>
                        <p><fmt:message key="label.profile" /></p>
                    </a>
                </li>
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_faculties">
                        <i class="material-icons">domain</i>
                        <p><fmt:message key="label.faculties" /></p>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=logout">
                        <i class="material-icons">exit_to_app</i>
                        <p><fmt:message key="label.logout" /></p>
                    </a>
                </li>
                <!-- your sidebar here -->
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <%@include file="/jsp/modules/navbar_main.jspf"%>
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-sm-12 float-right">
                        <div class="card card-stats">
                            <div class="card-header card-header-warning card-header-icon">
                                <div class="card-icon">
                                    <i class="material-icons">person</i>
                                </div>
                                <p class="card-category"><fmt:message key="label.profile.currentuser" /></p>
                                <h3 class="card-title">${sessionScope.user.firstName} ${sessionScope.user.lastName}</h3>
                            </div>
                            <div class="card-footer">
                                <div class="stats">
                                    <i class="material-icons">school</i>
                                    <span class="label-on-right">${sessionScope.role}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-4 col-sm-12 float-left">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h3 class="card-title">
                                    Enrollee marks
                                </h3>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead class="text-primary">
                                            <tr>
                                                <th>Subject ID</th>
                                                <th>Score</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${sessionScope.enrollee.marks}" var="subject">
                                                <tr>
                                                    <c:choose>
                                                        <c:when test="${sessionScope.locale eq 'ru_RU'}">
                                                            <td>${subject.key.nameRu}</td>
                                                        </c:when>
                                                        <c:when test="${sessionScope.locale eq 'en_US'}">
                                                            <td>${subject.key.nameEn}</td>
                                                        </c:when>
                                                    </c:choose>
                                                    <td>${subject.value}</td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${sessionScope.availableSubjects.size() > 0}">
                                                <tr>
                                                    <td colspan="2">
                                                        <div class="form-row">
                                                            <div class="form-group col-8">
                                                                <select class="form-control" id="subjectSelect" form="subject_form" name="subjectToAdd">
                                                                    <c:forEach items="${sessionScope.availableSubjects}" var="availableSubject">
                                                                        <option>
                                                                            (ID: ${availableSubject.id})
                                                                            <c:choose>
                                                                                <c:when test="${sessionScope.locale eq 'ru_RU'}">
                                                                                    ${availableSubject.nameRu}
                                                                                </c:when>
                                                                                <c:when test="${sessionScope.locale eq 'en_US'}">
                                                                                    ${availableSubject.nameEn}
                                                                                </c:when>
                                                                            </c:choose>
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="form-group col-4">
                                                                <input type="text" class="form-control" placeholder="score" form="subject_form" name="subjectScore" pattern="^([0-9]{1,2})|(100)$" required />
                                                            </div>
                                                        </div>
                                                        <button type="submit" class="btn btn-primary" name="command" value="add_subject" form="subject_form">Add subject</button>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
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
                <%@ include file="/jsp/modules/copyright.jspf"%>
            </div>
        </footer>
    </div>
</div>
<form action="/controller" method="get" id="subject_form"></form>
<!--   Core JS Files   -->
<script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/core/popper.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/core/bootstrap-material-design.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
<!-- Control Center for Material Dashboard: parallax effects, scripts for the example pages etc -->
<script src="${pageContext.request.contextPath}/assets/js/material-dashboard.min.js?v=2.1.0" type="text/javascript"></script>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
</body>

</html>
