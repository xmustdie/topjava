<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>

<body>

<h2> Edit/Create meal</h2>

<form method="POST" action='someAction' name="addMeal">
    <table width="300px">
        <tr>
            <td width="150px">Date</td>
            <td>
                <input type="datetime-local" name="datetime-local" path="dueDate" class="date"
                       value="<fmt:formatDate value="${empty meal.dateTime ? ' ' : meal.dateTime}"
                       pattern="MM-dd-yyyy HH:mm"
                       />"/>
            </td>
        </tr>
        <tr>
            <td width="150px">Description</td>
            <td>
                <input type="text" name="description"
                       value="<c:out value="${meal.description}" />"/>
            </td>
        </tr>
        <tr>
            <td width="150px">Calories</td>
            <td>
                <input type="text" name="calories"
                       value="<c:out value="${meal.calories}" />"/>
            </td>
        </tr>
        <tr>
            <td width="150px"></td>
            <td width="150px" align="right">
                <input type="submit" value="  ADD  ">
                <input type="button" value=" Cancel "
                       onclick="window.location.href = '/'"/>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
