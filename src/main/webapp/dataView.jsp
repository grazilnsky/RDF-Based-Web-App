<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>General Queries - Smart City</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/style.css"> </link>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <h3>The records at the ${requestScope.hospitalInput} hospital.</h3>
        <p></p>
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

            <c:forEach items="${requestScope.hospitalRec}" var="record">
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
        <h3>The records at the coordinates ${requestScope.coordsInput}.</h3>
        <p></p>
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

            <c:forEach items="${requestScope.coordsRec}" var="record">
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

        <h3>The records created in the last hour.</h3>
        <p></p>
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

            <c:forEach items="${requestScope.timeRec}" var="record">
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

        <h3>The place with the most records</h3>
        <p></p>
        <table>    
            <tr>
                <th>Location</th>
                <th># Records</th>
            </tr>

            <c:forEach items="${requestScope.mostCoord}" var="record">
                <tr>
                    <td>${record.getLocation()}</td>
                    <td>${record.getCounter()}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
