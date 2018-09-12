<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 11.09.2018
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <table>
        <tr>
            <td>Failed request from:</td><td>${pageContext.errorData.requestURI}</td>
        </tr>
        <tr>
            <td>Servlet name or type:</td><td>${pageContext.errorData.servletName}</td>
        </tr>
        <tr>
            <td>Status code:</td><td>${pageContext.errorData.statusCode}</td>
        </tr>
        <tr>
            <td>Exception:</td><td>${pageContext.errorData.throwable}</td>
        </tr>
    </table>
</body>
</html>
