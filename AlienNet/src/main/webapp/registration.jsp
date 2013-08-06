<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Alien register</title>
    </head>
    <body>
        <h1>Hello Alien! Register, please.</h1>
        <form:form method="post"  commandName="alien">

            <table>
                <tr>
                    <td><form:label path="name">Name</form:label></td>
                    <td><form:input path="name" /></td> 
                </tr>
                <tr>
                    <td><form:label path="password">Your password</form:label></td>
                    <td><form:input path="password" /></td>
                </tr>
                <tr>
                    <td><form:label path="lang">Your language</form:label></td>
                    <td><form:input path="lang" /></td>
                </tr>
                <tr>
                    <td><form:label path="address">Address</form:label></td>
                    <td><form:input path="address" /></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Register"/>
                    </td>
                </tr>
            </table>  

        </form:form>

    </body>
</html>
