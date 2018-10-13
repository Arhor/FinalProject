<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
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
    <%@ include file="/jsp/modules/core_style.jspf"%>
</head>

<body class="">
<c:if test="${sessionScope.role eq 'CLIENT' || sessionScope.role eq 'ADMIN'}">
    <!-- Modal -->
    <%@ include file="/jsp/modules/SessionExpiredModal.jspf"%>
    <c:if test="${sessionScope.role eq 'CLIENT'}">
        <%@ include file="/jsp/modules/facultyModal.jspf"%>
    </c:if>
</c:if>
<div class="wrapper ">
    <div class="sidebar" data-color="purple" data-background-color="white">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=first">
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
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <%@include file="/jsp/modules/navbar_main.jspf"%>
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-sm-12 mr-auto ml-auto">
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
                                                <th class="text-center"><fmt:message key="label.faculties.table.subjects" /></th>
                                                <c:choose>
                                                    <c:when test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                        <th class="text-center"><fmt:message key="label.actions" /></th>
                                                    </c:when>
                                                    <c:when test="${sessionScope.role.toString() eq 'ADMIN'}">
                                                        <th class="text-center"><fmt:message key="label.status" /></th>
                                                    </c:when>
                                                </c:choose>
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
                                                            <c:choose>
                                                                <c:when test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                                    <td class="td-actions text-center">
                                                                        <button type="button" class="btn btn-success" style="display : none" id="fac${faculty.id}">
                                                                            <i class="material-icons">edit</i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-danger" style="display : none" id="fac${faculty.id}">
                                                                            <i class="material-icons">close</i>
                                                                        </button>
                                                                    </td>
                                                                </c:when>
                                                                <c:when test="${sessionScope.role.toString() eq 'ADMIN'}">
                                                                    <td class="td-actions text-center">
                                                                        <c:choose>
                                                                            <c:when test="${faculty.checked}">
                                                                                <a href="${pageContext.request.contextPath}/controller?command=show_faculty_statistics&facultyId=${faculty.id}" class="btn btn-warning btn-block" role="button"><fmt:message key="label.faculties.table.results.show" /> <i class="material-icons">done_outline</i></a>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <a href="${pageContext.request.contextPath}/controller?command=define_result&facultyId=${faculty.id}" class="btn btn-info btn-block" role="button"><fmt:message key="label.faculties.table.results.define" /> <i class="material-icons">toc</i></a>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </c:when>
                                                            </c:choose>
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
                                                            <c:choose>
                                                                <c:when test="${sessionScope.role.toString() eq 'CLIENT'}">
                                                                    <td class="td-actions text-center">
                                                                        <button type="button" class="btn btn-success" style="display : none" id="fac${faculty.id}">
                                                                            <i class="material-icons">edit</i>
                                                                        </button>
                                                                        <button type="button" class="btn btn-danger" style="display : none" id="fac${faculty.id}">
                                                                            <i class="material-icons">close</i>
                                                                        </button>
                                                                        <br />
                                                                    </td>
                                                                </c:when>
                                                                <c:when test="${sessionScope.role.toString() eq 'ADMIN'}">
                                                                    <td class="td-actions text-center">
                                                                        <c:choose>
                                                                            <c:when test="${faculty.checked}">
                                                                                <a href="${pageContext.request.contextPath}/controller?command=show_faculty_statistics&facultyId=${faculty.id}" class="btn btn-warning btn-block" role="button"><fmt:message key="label.faculties.table.results.show" /> <i class="material-icons">done_outline</i></a>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <a href="${pageContext.request.contextPath}/controller?command=define_result&facultyId=${faculty.id}" class="btn btn-info btn-block" role="button"><fmt:message key="label.faculties.table.results.define" /> <i class="material-icons">toc</i></a>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                </c:when>
                                                            </c:choose>
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
                </form>
            </c:if>
            <c:if test="${sessionScope.role eq 'ADMIN'}">
                <form action="${pageContext.request.contextPath}/controller" id="defineResult" method="post"></form>
            </c:if>
        </div>
        <%@ include file="/jsp/modules/footer.jspf"%>
    </div>
</div>
<%@ include file="/jsp/modules/core_js.jspf"%>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
<!-- Ajax -->
<c:if test="${sessionScope.role eq 'CLIENT'}">
    <script src="${pageContext.request.contextPath}/assets/js/faculties-client-ajax.js" type="text/javascript"></script>
</c:if>
</body>

</html>
