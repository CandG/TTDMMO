<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Server settings</title>
    </head>
    <body>
        <h1>Server settings</h1>
        <ul>
            <li><a href="${pageContext.request.contextPath}/server/start">start</a></li>            
            <li><a href="${pageContext.request.contextPath}/server/stop">stop</a></li>
        </ul>
        <p>Status</p>
        <table class="mytable">
            <c:forEach items="${mylist}" var="s" varStatus="i">
                <tr>
                    <td><c:out value="${s}"/></td>
                </tr>
            </c:forEach>
        </table>
        <br> <br>
        Other settings (executed if server is running): 
        <form action="${pageContext.request.contextPath}/server/generate" method="GET">
            Offset (user ID from firebase): <input type="text" name="offset" value="0">
            <input type="submit" value="Create new game (old one will be deleted)">
        </form>
    </body>
</html>