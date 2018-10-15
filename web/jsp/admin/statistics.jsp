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
        <fmt:message key="label.statistics.title" />
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
                                <h4 class="card-title"><fmt:message key="label.statistics.title" /></h4>
                                <p class="card-category"><fmt:message key="label.statistics.description" />
                                    <c:choose>
                                        <c:when test="${sessionScope.locale eq 'en_US'}">
                                            ${requestScope.faculty.nameEn}
                                        </c:when>
                                        <c:when test="${sessionScope.locale eq 'ru_RU'}">
                                            ${requestScope.faculty.nameRu}
                                        </c:when>
                                    </c:choose>
                                </p>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead class="text-primary">
                                            <tr>
                                                <th><fmt:message key="label.user.id" /></th>
                                                <th><fmt:message key="label.email" /></th>
                                                <th><fmt:message key="label.name.first" /></th>
                                                <th><fmt:message key="label.name.last" /></th>
                                                <th><fmt:message key="label.client.profile.country" /></th>
                                                <th><fmt:message key="label.client.profile.city" /></th>
                                                <th><fmt:message key="label.score.total" /></th>
                                                <th><fmt:message key="label.status" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requestScope.resultSet}" var="userEntry">
                                                <tr>
                                                    <td>${userEntry.key.id}</td>
                                                    <td>${userEntry.key.email}</td>
                                                    <td>${userEntry.key.firstName}</td>
                                                    <td>${userEntry.key.lastName}</td>
                                                    <c:forEach items="${userEntry.value}" var="enrolleEntry">
                                                        <td>${enrolleEntry.key.country}</td>
                                                        <td>${enrolleEntry.key.city}</td>
                                                        <td>${enrolleEntry.key.marks.values().stream().map(x -> x).sum() + enrolleEntry.key.schoolCertificate}</td>
                                                        <c:choose>
                                                            <c:when test="${enrolleEntry.value eq 'none'}">
                                                                <td class="text-danger font-weight-bold">${enrolleEntry.value}</td>
                                                            </c:when>
                                                            <c:when test="${enrolleEntry.value eq 'paid'}">
                                                                <td class="text-primary font-weight-bold">${enrolleEntry.value}</td>
                                                            </c:when>
                                                            <c:when test="${enrolleEntry.value eq 'budget'}">
                                                                <td class="text-success font-weight-bold">${enrolleEntry.value}</td>
                                                            </c:when>
                                                        </c:choose>
                                                    </c:forEach>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@ include file="/jsp/modules/footer.jspf"%>
    </div>
</div>
<%@ include file="/jsp/modules/core_js.jspf"%>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
</body>

</html>
