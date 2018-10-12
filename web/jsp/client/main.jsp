<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <fmt:message key="label.title" />
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
                                    <fmt:message key="label.subject.title" />
                                </h3>
                                <p class="card-category">
                                    <fmt:message key="label.subject.description" />
                                </p>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead class="text-primary">
                                            <tr>
                                                <th><fmt:message key="label.subject.name" /></th>
                                                <th><fmt:message key="label.subject.score" /></th>
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
                                            <c:if test="${sessionScope.availableSubjects.size() > 0 && sessionScope.enrollee.marks.size() < 3}"> <%-- there are only 3 subjects allowed to pass --%>
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
                                                        <button type="submit" class="btn btn-primary" name="command" value="add_subject" form="subject_form"><fmt:message key="label.client.subject.add" /></button>
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
        <%@ include file="/jsp/modules/footer.jspf"%>
    </div>
</div>
<form action="${pageContext.request.contextPath}/controller" method="get" id="subject_form"></form>
<%@ include file="/jsp/modules/core_js.jspf"%>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
</body>

</html>
