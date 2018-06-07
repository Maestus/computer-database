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

	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard?page=1"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">

			<form class="form-inline" action="<c:url value='logout' />" method="post">
				<input type="submit" value="Log out" /> <input type="hidden"
					name="${_csrf.parameterName}" value="${_csrf.token}" />
			</form>

			<a class="pull-right btn btn-default" href="?locale=en"> <spring:message
					code="app.lang.english" />
			</a> <a class="pull-right btn btn-default" href="?locale=fr"> <spring:message
					code="app.lang.french" />
			</a>
			<h1 id="homeTitle">
				<c:out value="${numberOfElement}" />
				<spring:message code="app.dashboard.computerEntries" />
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="search?page=1" method="GET"
						class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" required /> <input
							type="submit" id="searchsubmit"
							value=<spring:message code="app.dashboard.button.filter"/>
							class="btn btn-primary" />
						<div class="btn-group btn-group-toggle" data-toggle="buttons">
							<label class="btn btn-secondary active"> <input
								type="radio" name="searchBy" value="forComputer" id="option1"
								autocomplete="off" checked> <spring:message
									code="app.dashboard.searchBy.computer" />
							</label> <label class="btn btn-secondary"> <input type="radio"
								name="searchBy" value="forCompany" id="option2"
								autocomplete="off"> <spring:message
									code="app.dashboard.searchBy.company" />
							</label>
						</div>
					</form>
				</div>

				<div class="pull-right">
					<c:if test="${searchProcess}">
						<a class="btn btn-secondary" href="dashboard?page=1">Complete
							List</a>
					</c:if>

					<a class="btn btn-success" id="addComputer" href="add"><spring:message
							code="app.dashboard.button.addComputer" /></a> <a
						class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();"><spring:message
							code="app.dashboard.button.edit" /></a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="delete" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="glyphicon glyphicon-trash"></i>
							</a>
						</span></th>
						<th><spring:message code="app.dashboard.table.computerName" /></th>
						<th><spring:message
								code="app.dashboard.table.computerIntroduced" /></th>
						<!-- Table header for Discontinued Date -->
						<th><spring:message
								code="app.dashboard.table.computerDiscontinued" /></th>
						<!-- Table header for Company -->
						<th><spring:message
								code="app.dashboard.table.computerCompany" /></th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">

					<c:forEach items="${ companyWithComputers }" var="titre"
						varStatus="status">

						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${ titre.getComputer().getId() }"></td>
							<td><a
								href="edit?id=<c:out value="${ titre.getComputer().getId() }" />"
								onclick=""><c:out value="${ titre.getComputer().getName() }" /></a>
							</td>
							<td><c:out value="${ titre.getComputer().getIntroduced() }" /></td>
							<td><c:out
									value="${ titre.getComputer().getDiscontinued() }" /></td>
							<td><c:out value="${ titre.getCompany().getName() }" /></td>
						</tr>

					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>
	<footer class="navbar">
		<div class="container text-center">

			<c:if test="${searchProcess}">
				<c:url var="searchUri"
					value="search?search=${search}&searchBy=${searchBy}&page=##" />
			</c:if>
			<c:if test="${!searchProcess}">
				<c:url var="searchUri" value="${'dashboard?page=##' }" />
			</c:if>
			<mylib:pagination maxLinks="10" currPage="${page}"
				totalPages="${nbPage}" uri="${searchUri}" />
		</div>

		<!-- <div class="btn-group btn-group-sm pull-right" role="group" > -->
		<form class="btn-group btn-group-sm pull-right" action="dashboard"
			method="POST">
			<input name="b1" type="submit" class="btn btn-default" value="10" />
			<input name="b2" type="submit" class="btn btn-default" value="50" />
			<input name="b3" type="submit" class="btn btn-default" value="100" />
		</form>
	</footer>

	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/dashboard.js"></script>

</body>
</html>