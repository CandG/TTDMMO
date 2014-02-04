<%@page import="java.util.List"%>
<%@page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>My JSP page</title>
        <style>
            #myheading { color: red; }   
            table.mytable { border-collapse: collapse; margin: 10px; }
            table.mytable th, table.mytable td {  border: solid 1px black; padding: 4px; }
        </style>
        <script>
            console.log("Hello JavaScript console !");
        </script>
    </head>
    <body>
        <h1>Server settings</h1>
        <ul>
            <li><a href="${pageContext.request.contextPath}/server/start">start</a></li>
            <li><a href="${pageContext.request.contextPath}/server/stop">stop</a></li>
        </ul>
        <p>Servers</p>
        <table class="mytable">
            <c:forEach items="${mylist}" var="s" varStatus="i">
                <tr>
                    <td>${i.count}</td>
                    <td><c:out value="${s}"/></td>
                </tr>
            </c:forEach>

        </table>

    </body>
</html>