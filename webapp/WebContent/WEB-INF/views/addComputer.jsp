<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="resources/css/bootstrap.min.css" rel="stylesheet"
	media="screen">
<link href="resources/css/font-awesome.css" rel="stylesheet"
	media="screen">
<link href="resources/css/main.css" rel="stylesheet" media="screen">
<script src="resources/js/jquery.min.js"></script>
<script src="resources/js/jquery.validate.min.js"></script>
<script src="resources/js/validator.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard?page=1"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>
						<spring:message code="app.addComputer.title" />
					</h1>

					<c:if test="${created}">
						<div class="alert alert-success">
							<strong><spring:message code="app.message.add.success" /></strong>
						</div>
					</c:if>

					<c:if test="${dateError}">
						<div class="alert alert-danger">
							<strong><spring:message code="app.message.dateError" /></strong>
						</div>
					</c:if>

					<form action="add" method="POST" name="computerForm">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><spring:message
										code="app.addComputer.name" /></label> <input type="text"
									class="form-control" name="computerName" id="computerName"
									placeholder="Computer name" />
							</div>
							<div class="form-group">
								<label for="introduced"><spring:message
										code="app.addComputer.introduced" /></label> <input type="date"
									class="form-control" name="introduced" id="introduced"
									placeholder="Introduced date" />
							</div>
							<div class="form-group">
								<label for="discontinued"><spring:message
										code="app.addComputer.discontinued" /></label> <input type="date"
									class="form-control" name="discontinued" id="discontinued"
									placeholder="Discontinued date" />
							</div>
							<div class="form-group">
								<label for="companyId"><spring:message
										code="app.addComputer.company" /></label> <select
									class="form-control" id="companyId" name="companyId">
									<c:forEach items="${ companies }" var="company"
										varStatus="status">
										<option
											value="<c:out value="${ company.getCompany().getId() }" />"><c:out
												value="${ company.getCompany().getName() }" /></option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit"
								value=<spring:message
                            code="app.addComputer.button.add" />
								class="btn btn-primary">
							<spring:message code="app.addComputer.choice" />
							<a href="dashboard?page=1" class="btn btn-default"><spring:message
									code="app.addComputer.button.cancel" /></a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
</body>
</html>