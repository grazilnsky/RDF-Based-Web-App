<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Records - Smart City</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/style.css"> </link>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>

        <table>
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Location</th>
                <th>Coordinates</th>
                <th>Date</th>
                <th>Time</th>
                <th>Duration</th>
            </tr>

            <c:forEach items="${requestScope.records}" var="record">
                <c:choose> 
                    <c:when test = "${record.isThisWeek() == true}">
                        <tr>
                            <td style="background-color:green">${record.getName()}</td>
                            <td>${record.getType()}</td>
                            <td>${record.getLocation()}</td>
                            <td>${record.getCoordinates()}</td>
                            <td>${record.getDate()}</td>
                            <td>${record.getTime()}</td>
                            <td>${record.getDuration()}</td>
                        </tr>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td style="background-color:yellow">${record.getName()}</td>
                            <td>${record.getType()}</td>
                            <td>${record.getLocation()}</td>
                            <td>${record.getCoordinates()}</td>
                            <td>${record.getDate()}</td>
                            <td>${record.getTime()}</td>
                            <td>${record.getDuration()}</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </table>
        <form action="FilterController" method="post">
            <br>
            <h3>Select which hospital's records you would like to see.</h3>
            <br>
            <select name="hospitalInput" id="hospitalInput">
                <c:forEach items="${hospitals}" var="hospital">
                    <option value = "<c:out value="${hospital}"/>"><c:out value="${hospital}"/></option>
                </c:forEach>
            </select>
            <br>
            <h3>Select the coordinates where you want to see the last 10 records.</h3>
            <br>
            <select name="coordinatesInput" id="coordinatesInput">
                <c:forEach items="${coordinates}" var="coordinates">
                    <option value = "<c:out value="${coordinates}"/>"><c:out value="${coordinates}"/></option>
                </c:forEach>
            </select>
            <br>
            <p></p>
            <div class="form-element">
                <input type="submit" value="Retrieve Records">
            </div>
        </form>
    </body>
</html>
