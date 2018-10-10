<%@ page contentType="text/html;charset=UTF-8" %>
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
        <fmt:message key="label.admin.profile.title" />
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
                    <div class="col-md-8 mr-auto ml-auto">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h4 class="card-title"><fmt:message key="label.admin.profile.title" /></h4>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/controller" method="POST">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="email" class="bmd-label-floating"><fmt:message key="label.email" /></label>
                                                <input type="email" class="form-control" id="email" value="${sessionScope.user.email}" disabled />
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="password" class="bmd-label-floating"><fmt:message key="label.password" /></label>
                                                <input type="password" class="form-control" id="password" name="password" required/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="firstName" class="bmd-label-floating"><fmt:message key="label.name.first" /></label>
                                                <input type="text" class="form-control" id="firstName" name="firstName" value="${sessionScope.user.firstName}" pattern="^[-а-яА-ЯёЁa-zA-Z]{2,35}$" required/>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="lastName" class="bmd-label-floating"><fmt:message key="label.name.last" /></label>
                                                <input type="text" class="form-control" id="lastName" name="lastName" value="${sessionScope.user.lastName}" pattern="^[-а-яА-ЯёЁa-zA-Z]{2,35}$" required/>
                                            </div>
                                        </div>
                                    </div>
                                    <span style="color: red">${requestScope.profileErrorMessage}</span>
                                    <button type="submit" class="btn btn-primary pull-right" name="command" value="update_profile"><fmt:message key="label.profile.update" /></button>
                                    <div class="clearfix"></div>
                                </form>
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

                        </li>
                    </ul>
                </nav>
                <%@ include file="/jsp/modules/copyright.jspf"%>
            </div>
        </footer>
    </div>
</div>
<%@ include file="/jsp/modules/core_js.jspf"%>
<!-- Clock -->
<script src="${pageContext.request.contextPath}/assets/js/clock.js" type="text/javascript"></script>
</body>

</html>