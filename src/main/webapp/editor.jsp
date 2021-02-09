<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>


<!DOCTYPE html>
<html>

<body>

<h2> Edit/Create meal</h2>


<c:set var="meal" target="${myMeal}"/>
<table width="400px">
    <tr>
        <td width="180px">Date</td>
        <td>
            <c:set var="dt" value="${meal}}"
            <input type="datetime-local" name="dateTime"
                   value="${meal.dateTime}?  "/>
        </td>
    </tr>
    <tr>
        <td width="180px">Description</td>
        <td>
            <input type="text" name="description"
                   value="${meal.description}"/>
        </td>
    </tr>
    <tr>
        <td width="180px">Calories</td>
        <td>
            <input type="text" name="calories"
                   value="${meal.calories}"/>
        </td>
    </tr>
    <tr>
        <td width="180px"></td>
        <td>
            <c:url var="saveButton" value="meals"/>
            <input type="submit" value="Save" onclick="window.location.href =
                    '${saveButton}'"/>
            <c:url var="cancelButton" value="meals"/>
            <input type="button" value="Cancel" onclick="window.location.href =
                    '${cancelButton}'"/>
        </td>
    </tr>
</table>


</body>
</html>
