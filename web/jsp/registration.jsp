<%--
  Created by IntelliJ IDEA.
  User: arhor
  Date: 12.9.18
  Time: 20.56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/style.css">
    <title>Registration page</title>
</head>
<body>
<div class="container">
    <form class="login-form" action="/controller" name="loginForm" method="POST">
        <input type="hidden" name="command" value="registration">
        <input name="login" type="text" placeholder="email" required />
        <input name="password" type="password" placeholder="password" required />
        <input name="firstName" type="text" placeholder="first name" required />
        <input name="lastName" type="text" placeholder="last name" required />

        ${errorLoginMessage}
        ${wrongAction}
        ${nullPage}
        <input type="submit" value="Sign in" />
    </form>
</div>
</body>
</html>
