<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
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
            </ul>
        </div>
    </div>
    <div class="main-panel">
        <%@include file="/jsp/modules/navbar_main.jspf"%>
        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-6 mr-auto ml-auto">
                        <div class="card">
                            <div class="card-header card-header-warning">
                                <h4 class="card-title">ERROR PAGE</h4>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table">
                                        <tr>
                                            <td>Failed request from:</td>
                                            <td>${pageContext.errorData.requestURI}</td>
                                        </tr>
                                        <tr>
                                            <td>Status code:</td>
                                            <td>${pageContext.errorData.statusCode}</td>
                                        </tr>
                                        <tr>
                                            <td>Description:</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${pageContext.errorData.statusCode eq 500}">
                                                        Internal server error
                                                    </c:when>
                                                    <c:when test="${pageContext.errorData.statusCode eq 403}">
                                                        Access forbidden
                                                    </c:when>
                                                    <c:when test="${pageContext.errorData.statusCode eq 404}">
                                                        Page not found
                                                    </c:when>
                                                    <c:otherwise>
                                                        Unknown error
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                        <c:if test="${pageContext.errorData.throwable != null}">
                                            <tr>
                                                <td>Exception:</td>
                                                <td>${pageContext.errorData.throwable}</td>
                                            </tr>
                                        </c:if>
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

</body>

</html>
