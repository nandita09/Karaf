<%@ page language="java" import="com.krf.pkg.MainServlet"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta charset="ISO-8859-1">
<title>User Time Line</title>
</head>
<body>
<h3>Followers</h3>
<%! MainServlet api = new MainServlet(); %>
<% api.application_only_auth(); %>
<%=MainServlet.apiGetHomeTimeline()%>

</body>
</html>