<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>welcome</title>
</head>
<body>
<p>users: ${users}</p>

<p>documents: ${documents}</p>

<p>orders: ${orders}</p>

<%--<p>URL: ${URL}</p>

<p>PathInfo: ${pathInfo}</p>

<p>SessionId: ${sessionId}</p>

<p>Parameters: ${parameters}</p>--%>
</body>
</html>
