<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <fmt:message key="label.registration.title" />
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
        <div class="logo">
            <span class="simple-text logo-normal">
                <fmt:message key="label.title" />
            </span>
        </div>
        <div class="sidebar-wrapper">
            <ul class="nav">
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=home">
                        <i class="material-icons">home</i>
                        <p><fmt:message key="label.homepage" /></p>
                    </a>
                </li>
                <li class="nav-item active  ">
                    <a class="nav-link" href="${pageContext.request.contextPath}/controller?command=show_faculties">
                        <i class="material-icons">domain</i>
                        <p><fmt:message key="label.faculties" /></p>
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
                    <div class="col-lg-4 col-md-4 ml-auto mr-auto">
                        <div class="card">
                            <div class="card-header card-header-primary">
                                <h3 class="card-title"><fmt:message key="label.registration.title" /></h3>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/controller" name="registrationForm" method="POST">
                                    <div class="row">
                                        <div class="col-md-8 col-sm-6">
                                            <div class="form-group bmd-form-group">
                                                <label for="email" class="bmd-label-floating"><fmt:message key="label.email" /></label>
                                                <input class="form-control" id="email" name="email" type="email" pattern="[-a-z0-9!#$%&'*+/=?^_`{|}~]+(\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@([a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\.)*([a-z]{2,4})" required />
                                            </div>
                                        </div>
                                        <div class="col-md-4 col-sm-6">
                                            <div class="form-group">
                                                <label for="langSelector"><fmt:message key="label.lang" /></label>
                                                <select class="form-control" id="langSelector" name="language">
                                                    <option selected>RU</option>
                                                    <option>EN</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group bmd-form-group">
                                                <label for="password" class="bmd-label-floating"><fmt:message key="label.password" /></label>
                                                <input class="form-control" id="password" name="password" type="password" pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{6,10}$" required />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group bmd-form-group">
                                                <label for="firstName" class="bmd-label-floating"><fmt:message key="label.name.first" /></label>
                                                <input class="form-control" id="firstName" name="firstName" type="text" pattern="^[-а-яА-ЯёЁa-zA-Z]{2,35}$" required />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group bmd-form-group">
                                                <label for="lastName" class="bmd-label-floating"><fmt:message key="label.name.last" /></label>
                                                <input class="form-control" id="lastName" name="lastName" type="text" pattern="^[-а-яА-ЯёЁa-zA-Z]{2,35}$" required />
                                            </div>
                                        </div>
                                    </div>
                                    ${requestScope.registrationError}
                                    <div class="row">
                                        <button class="btn btn-primary col-lg-8 col-md-8 ml-auto mr-auto" name="command" value="register"><fmt:message key="label.register" /></button>
                                    </div>
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
</body>

</html>