<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri = "http://java.sun.com/jsp/jstl/core"%>
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
<!-- Modal -->
<%@ include file="/jsp/modules/SessionExpiredModal.jspf"%>
<div class="wrapper ">
    <div class="sidebar" data-color="purple" data-background-color="white">
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
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=first">
                        <i class="material-icons">people</i>
                        <p><fmt:message key="label.admin.users" /></p>
                    </a>
                </li>
                <li class="nav-item active">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=logout">
                        <i class="material-icons">exit_to_app</i>
                        <p><fmt:message key="label.logout" /></p>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <%@include file="/jsp/modules/navbar_main.jspf"%>
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
                                            <th class="text-center"><fmt:message key="label.user.role" /></th>
                                            <th class="text-center"><fmt:message key="label.status" /></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${requestScope.users}" var="currUser">
                                            <tr class="admission-user" id="${currUser.id}">
                                                <td>${currUser.id}</td>
                                                <td>${currUser.email}</td>
                                                <td>${currUser.firstName}</td>
                                                <td>${currUser.lastName}</td>
                                                <td class="text-center">${currUser.role}</td>
                                                <td class="td-actions text-center">
                                                    <c:choose>
                                                        <c:when test="${currUser.role.toString() eq 'ADMIN'}">
                                                            <button type="button" class="btn btn-success" style="display : none" id="uid${currUser.id}" disabled>
                                                                <i class="material-icons">person</i>
                                                            </button>
                                                            <button type="button" class="btn btn-danger" style="display : none" id="uid${currUser.id}" disabled>
                                                                <i class="material-icons">close</i>
                                                            </button>
                                                        </c:when>
                                                        <c:when test="${currUser.role.toString() eq 'CLIENT'}">
                                                            <button type="button" class="btn btn-success" style="display : none" id="uid${currUser.id}">
                                                                <i class="material-icons">person</i>
                                                            </button>
                                                            <button type="button" class="btn btn-danger" style="display : none" id="uid${currUser.id}">
                                                                <i class="material-icons">close</i>
                                                            </button>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
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
                                                    <a class="page-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=prev" tabindex="-1"><fmt:message key="label.prev" /></a>
                                                </li>
                                            </c:when>
                                            <c:when test="${sessionScope.pageNum > 0}">
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=prev"><fmt:message key="label.prev" /></a>
                                                </li>
                                            </c:when>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${sessionScope.pageNum >= sessionScope.pageMax}">
                                                <li class="page-item disabled">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=next" tabindex="-1"><fmt:message key="label.next" /></a>
                                                </li>
                                            </c:when>
                                            <c:when test="${sessionScope.pageNum < sessionScope.pageMax}">
                                                <li class="page-item">
                                                    <a class="page-link" href="${pageContext.request.contextPath}/controller?command=show_users&target_page=next"><fmt:message key="label.next" /></a>
                                                </li>
                                            </c:when>
                                        </c:choose>
                                    </ul>
                                </nav>
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
<!-- Ajax -->
<script src="${pageContext.request.contextPath}/assets/js/users-ajax.js" type="text/javascript"></script>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
</body>

</html>