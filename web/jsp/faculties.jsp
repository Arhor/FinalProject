<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 16.09.2018
  Time: 3:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />
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

<c:if test="${sessionScope.role eq 'CLIENT' || sessionScope.role eq 'ADMIN'}">
    <!-- Modal -->
    <%@ include file="/jsp/modules/SessionExpiredModal.jspf"%>
</c:if>

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
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admission?command=home">
                        <i class="material-icons">home</i>
                        <p><fmt:message key="label.homepage" /></p>
                    </a>
                </li>
                <c:if test="${sessionScope.role.toString() eq 'ADMIN' || sessionScope.role.toString() eq 'CLIENT' }" >
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admission?command=profile">
                            <i class="material-icons">person</i>
                            <p><fmt:message key="label.profile" /></p>
                        </a>
                    </li>
                </c:if>
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_faculties">
                        <i class="material-icons">domain</i>
                        <p><fmt:message key="label.faculties" /></p>
                    </a>
                </li>
                <c:if test="${sessionScope.role.toString() eq 'ADMIN'}">
                    <li class="nav-item active  ">
                        <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_users">
                            <i class="material-icons">people</i>
                            <p><fmt:message key="label.admin.users" /></p>
                        </a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.role.toString() eq 'CLIENT' || sessionScope.role.toString() eq 'ADMIN'}">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=logout">
                            <i class="material-icons">exit_to_app</i>
                            <p><fmt:message key="label.logout" /></p>
                        </a>
                    </li>
                </c:if>
                <!-- your sidebar here -->
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <%@include file="/jsp/modules/navbar_main.jspf"%>
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-10 col-md-10 col-sm-12 mr-auto ml-auto">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h4 class="card-title"><fmt:message key="label.faculties" /></h4>
                                <p class="card-category"><fmt:message key="label.faculties.table.description" /></p>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead class="text-primary">
                                            <tr>
                                                <th><fmt:message key="label.faculties.table.id" /></th>
                                                <th><fmt:message key="label.faculties.table.faculty" /></th>
                                                <th class="text-center"><fmt:message key="label.faculties.table.payable" /></th>
                                                <th class="text-center"><fmt:message key="label.faculties.table.free" /></th>
                                                <th class="text-center">Subjects</th>
                                                <c:if test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                    <th class="text-center"><fmt:message key="label.actions" /></th>
                                                </c:if>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.faculties}" var="faculty">
                                                <c:choose>
                                                    <c:when test="${sessionScope.locale eq 'ru_RU'}" >
                                                        <tr class="faculty" id="${faculty.id}">
                                                            <td>${faculty.id}</td>
                                                            <td>${faculty.nameRu}</td>
                                                            <td class="text-center">${faculty.seatsPaid}</td>
                                                            <td class="text-center">${faculty.seatsBudget}</td>
                                                            <td class="text-center">
                                                                <c:forEach items="${faculty.subjects}" var="subject">
                                                                    <div>${subject.nameRu}</div>
                                                                </c:forEach>
                                                            </td>
                                                            <c:if test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                                <td class="td-actions text-center">
                                                                    <button type="button" rel="tooltip" class="btn btn-success" style="display : none" id="fac${faculty.id}">
                                                                        <i class="material-icons">edit</i>
                                                                    </button>
                                                                    <button type="button" rel="tooltip" class="btn btn-danger" style="display : none" id="fac${faculty.id}">
                                                                        <i class="material-icons">close</i>
                                                                    </button>
                                                                </td>
                                                            </c:if>
                                                        </tr>
                                                    </c:when>
                                                    <c:when test="${sessionScope.locale eq 'en_US'}" >
                                                        <tr class="faculty" id="${faculty.id}">
                                                            <td class="">${faculty.id}</td>
                                                            <td>${faculty.nameEn}</td>
                                                            <td class="text-center">${faculty.seatsPaid}</td>
                                                            <td class="text-center">${faculty.seatsBudget}</td>
                                                            <td class="text-center">
                                                                    <c:forEach items="${faculty.subjects}" var="subject">
                                                                        <div>${subject.nameEn}</div>
                                                                    </c:forEach>
                                                            </td>
                                                            <c:if test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                                <td class="td-actions text-center">
                                                                    <button type="button" rel="tooltip" class="btn btn-success" style="display : none" id="fac${faculty.id}">
                                                                        <i class="material-icons">edit</i>
                                                                    </button>
                                                                    <button type="button" rel="tooltip" class="btn btn-danger" style="display : none" id="fac${faculty.id}">
                                                                        <i class="material-icons">close</i>
                                                                    </button>
                                                                </td>
                                                            </c:if>
                                                        </tr>
                                                    </c:when>
                                                </c:choose>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${sessionScope.role eq 'CLIENT'}">
                <form>
                    <input type="hidden" id="enrolleeID" value="${sessionScope.enrollee.id}" />
                    <c:forEach items="${sessionScope.enrollee.marks.entrySet()}" var="subject">
                        <input type="hidden" class="subjectID" value="${subject.key}" />
                    </c:forEach>
                </form>
            </c:if>
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
<!--   Core JS Files   -->
<script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/core/popper.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/core/bootstrap-material-design.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
<!-- Control Center for Material Dashboard: parallax effects, scripts for the example pages etc -->
<script src="${pageContext.request.contextPath}/assets/js/material-dashboard.min.js?v=2.1.0" type="text/javascript"></script>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
<!-- Ajax -->
<script src="${pageContext.request.contextPath}/assets/js/app-ajax.js" type="text/javascript"></script>
</body>

</html>
