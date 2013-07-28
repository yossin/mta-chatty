<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String userAgent = request.getHeader("user-agent");
	Boolean mobile = false;

	if (userAgent.matches(".*Android.*") || userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*")) 
		mobile = true;

%>

<% 	if (mobile){ %>
<jsp:include page="jsp/mobile.jsp" />
<%	} else { %> 
<jsp:include page="jsp/desktop.jsp" />
<% 	} %>
