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
        <fmt:message key="label.confirmation.title" />
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
                                <h3 class="card-title"><fmt:message key="label.confirmation.title" /></h3>
                                <p class="card-category"><fmt:message key="label.confirmation.description" /></p>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/controller" name="confirmationForm" method="POST">
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="form-group bmd-form-group">
                                                <label for="confirmationCode" class="bmd-label-floating"><fmt:message key="label.confirmation.code" /></label>
                                                <input class="form-control" id="confirmationCode" name="confirmationCode" type="text" required />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <button class="btn btn-primary col-lg-8 col-md-8 ml-auto mr-auto" name="command" value="confirm"><fmt:message key="label.confirmation.confirm" /></button>
                                    </div>
                                </form>
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