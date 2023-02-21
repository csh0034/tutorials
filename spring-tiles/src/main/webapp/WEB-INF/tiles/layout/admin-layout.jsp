<%@ page pageEncoding="UTF-8" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!doctype html>
<html lang="ko">
<head>
</head>
<body>
<header>
  <tiles:insertAttribute name="header"/>
</header>
<tiles:insertAttribute name="contents"/>
<footer>
  <tiles:insertAttribute name="footer"/>
</footer>
</body>
</html>
