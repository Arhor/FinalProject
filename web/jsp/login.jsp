<%--
  Created by IntelliJ IDEA.
  User: Maxim Burishinets
  Date: 11.09.2018
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/style.css">
    <title>Title</title>
</head>
<body>
    <div class="container">
        <form class="login-form" action="/controller" name="loginForm" method="POST">
            <input type="hidden" name="command" value="login">
            <input name="login" type="text" placeholder="login" required />
            <input name="password" type="password" placeholder="password" required />
            ${errorLoginMessage}
            ${wrongAction}
            ${nullPage}
            <input type="submit" value="Sign in" />
        </form>
    </div>
</body>
</html>
