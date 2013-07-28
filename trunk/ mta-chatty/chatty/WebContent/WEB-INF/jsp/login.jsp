<%@ page language="java" contentType="text/html; charset=windows-1255"
    pageEncoding="windows-1255"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1255">
<title>Admin Login</title>
</head>
<body>
<h1>Admin Login</h1>
<form action="<%=request.getAttribute("action")%>" method="post">
<input type="text" name="user"/>
<input type="password" name="pass"/>
<input type="submit" name="Login" value="Login">
</form>

</body>
</html>