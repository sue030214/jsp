<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>ログイン画面</title>
		<link rel="STYLESHEET" href="todo.css" type="text/css">
		<script type="text/javascript" src="login.js">
		</script>
	</head>
	<body onload="fieldChanged();">
		<h1>ログイン画面</h1>
		<hr>
		<div align="center">
			<table border="0">
				<form action="login" method="post">
					<input type="hidden" name="action" value="login_action">
					<tr>
						<th class="login_field">
							ユーザーID
						</th>
						<td class="login_field">
							<input type="text"
							name="user_id"
							value=""
							size="24"
							id="user_id"
							onkeyup="fieldChanged();"
							onchange="fieldChanged();">
						</td>
					</tr>
					<tr>
						<th class="login_field">
							パスワード
						</th>
						<td class="login_field">
							<input type="text"
							name="password"
							value=""
							size="24"
							id="password"
							onkeyup="fieldChanged();"
							onchange="fieldChanged();">
						</td>
					</tr>
					<tr>
						<td colspan="2" class="login_button">
							<input type="submit" value="ログイン" id="login">
						</td>
					</tr>
				</form>
			</table>
		</div>
	</body>
</html>