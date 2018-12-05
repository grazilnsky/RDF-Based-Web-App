<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Enter Record - SmartCity</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/style.css"> </link>
    </head>
    <body>
        <%@include file="/WEB-INF/jspf/navbar.jspf" %>
        <div class="container">
            <h2 id="login-heading" class="text-center">Enter Record</h2>



            <form action="RecordController" method="post">
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" name="name" id="name"
                           placeholder="Name" class="form-control" required>
                </div>
                <h3>Type</h3>
                <br>
                <div class="form-group">
                    <label for="type">Type</label>
                  <%--  <input type="text" name="type" id="type"
                           placeholder="type" class="form-control" required>--%>
                    <select name="type" id="type">
                        <option value="hospital">Hospital</option>
                        <option value="parking">Parking</option>
                    </select>
                </div>

                <br>
                <h3>Location</h3>
                <br>
                <div class="form-group">
                    <label for="location">Location</label>
                    <input type="text" name="location" id="location"
                           placeholder="location" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="coordinates">Coordinates</label>
                    <input type="text" name="coordinates" id="coordinates"
                           placeholder="coordinates" class="form-control" required>
                </div>
                <br>
                <h3>Date and Time</h3>
                <br>
                <div class="form-group">
                    <label for="date">Date</label>
                    <input type="date" name="date" id="date"
                           placeholder="date" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="time">Time</label>
                    <input type="time" name="time" id="time"
                           placeholder="time" class="form-control" required>
                </div>
                <div class="form-element">
                    <input type="submit" value="Add record">
                </div>
            </form>

    </body>
</html>
