<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>登陆-kankantu</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="style/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}

.form-signin {
	max-width: 300px;
	padding: 19px 29px 29px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin input[type="text"],.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin-bottom: 15px;
	padding: 7px 9px;
}
</style>
<link href="style/css/bootstrap-responsive.css" rel="stylesheet">

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="style/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="style/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="style/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="style/ico/apple-touch-icon-57-precomposed.png">
<link rel="shortcut icon" href="style/ico/favicon.png">
</head>

<body>
	<jsp:include page="head.jsp">
       	<jsp:param value="index" name="flag"/>
 	</jsp:include>

	<div class="container">
		<form class="form-signin" action="login.do" method="post">
			<h2 class="form-signin-heading">登陆信息</h2>
			<input type="text" class="input-block-level" placeholder="用户名"  id="user_name" name="user_name"> 
			<input type="password" class="input-block-level" placeholder="密码" id="password" name="password">
			<!-- <label class="checkbox"> <input type="checkbox" value="remember-me">Remember me</label> -->
			<button class="btn btn-large btn-primary" type="submit">登陆</button>&nbsp;&nbsp;<a href="signup.jsp">没有账号？</a>
		</form>

	</div>
	<!-- /container -->

	<!-- Le javascript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="style/js/jquery.js"></script>
	<script src="style/js/bootstrap-transition.js"></script>
	<script src="style/js/bootstrap-alert.js"></script>
	<script src="style/js/bootstrap-modal.js"></script>
	<script src="style/js/bootstrap-dropdown.js"></script>
	<script src="style/js/bootstrap-scrollspy.js"></script>
	<script src="style/js/bootstrap-tab.js"></script>
	<script src="style/js/bootstrap-tooltip.js"></script>
	<script src="style/js/bootstrap-popover.js"></script>
	<script src="style/js/bootstrap-button.js"></script>
	<script src="style/js/bootstrap-collapse.js"></script>
	<script src="style/js/bootstrap-carousel.js"></script>
	<script src="style/js/bootstrap-typeahead.js"></script>

</body>
</html>
