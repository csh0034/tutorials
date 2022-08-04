<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring Boot Application with JSP</title></head>
<body>
  Hello, Spring Boot App <br>
  <ul>
    <li>\${servletContextNow}: ${servletContextNow}</li>
    <li>\${requestNow}: ${requestNow}</li>
    <li>\${sessionNow}: ${sessionNow}</li>
    <li>\${modelNow}: ${modelNow}</li>
    <li>\${applicationScope.servletContextNow}: ${applicationScope.servletContextNow}</li>
    <li>\${requestScope.requestNow}: ${requestScope.requestNow}</li>
    <li>\${sessionScope.sessionNow}: ${sessionScope.sessionNow}</li>
  </ul>
</body>
</html>
