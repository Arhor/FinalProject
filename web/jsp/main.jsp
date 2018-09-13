<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 11.09.2018
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/normalize.css">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <title>Welcome</title>
</head>
<body>
    <h3>Welcome!</h3>
    <hr />
    ${user}, hello! current role: ${role}
    <hr />
    <form class="login-form" action="/controller" method="POST">
        <input type="submit" name="command" value="LOGOUT">
    </form>
</body>
</html>
