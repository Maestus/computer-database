<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mylib" uri="Paginator"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
</head>
<body>
	<div class="container col-sm-4 col-md-offset-5">
		<div class="row">
			<div class="well container col-sm-8">
				<legend>Sign In</legend>
				<c:if test="${error}">
					<div class="alert alert-error">Incorrect Username or
						Password!</div>
				</c:if>
				<form method="POST" action="<c:url value='/login' />"
					accept-charset="UTF-8" class="col-sm-4">
					<input type="text" id="username" name="username"
						placeholder="Username"> <input type="password"
						id="password" name="password" placeholder="Password">
					<button type="submit" name="submit" class="btn btn-info btn-block">Sign
						in</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>