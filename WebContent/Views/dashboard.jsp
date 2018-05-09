<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="mylib" uri="Paginator"%>
</head>
<body>

	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="pull-right navbar-brand" href="ComputerList?direct=index">
				Menu </a> <a class="navbar-brand" href="ComputerList?page=1">
				Application - Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${numberOfElement}" />
				Entries
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="ComputerList" method="GET"
						class="form-inline">
						<input type="search" id="searchbox" name="search" minlength="2"
							class="form-control" placeholder="Search name" required /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
							<div class="btn-group btn-group-toggle" data-toggle="buttons">
			                    <label class="btn btn-secondary active"> 
			                       <input type="radio" name="searchBy" value="forComputer" id="option1" autocomplete="off" checked> 
			                       Computer
			                    </label> 
			                    <label class="btn btn-secondary"> 
			                       <input type="radio" name="searchBy" value="forCompany" id="option2" autocomplete="off"> 
			                       Company
			                    </label>
			                </div>
					</form>
				</div>
				
				<div class="pull-right">
					<c:if test="${searchProcess}">
						<a class="btn btn-secondary" id="addComputer"
							href="ComputerList?page=1">Complete List</a>
					</c:if>

					<a class="btn btn-success" id="addComputer" href="ComputerAdd">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="#" method="POST">
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
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">

					<c:forEach items="${ companyWithComputers }" var="titre"
						varStatus="status">

						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="0"></td>
							<td><a
								href="edit?id=<c:out value="${ titre.getComputer().getId() }" />"
								onclick=""><c:out value="${ titre.getComputer().getNom() }" /></a>
							</td>
							<td><c:out value="${ titre.getComputer().getIntroduced() }" /></td>
							<td><c:out
									value="${ titre.getComputer().getDiscontinued() }" /></td>
							<td><c:out value="${ titre.getCompany().getNom() }" /></td>
						</tr>

					</c:forEach>

				</tbody>
			</table>
		</div>
	</section>
	<footer class="navbar">
		<div class="container text-center">

			<c:if test="${searchProcess}">
				<c:url var="searchUri" value="ComputerList?search=${search}&page=##" />
			</c:if>
			<c:if test="${!searchProcess}">
				<c:url var="searchUri" value="${'ComputerList?page=##' }" />
			</c:if>
			<mylib:pagination maxLinks="10" currPage="${page}"
				totalPages="${nbPage}" uri="${searchUri}" />
		</div>

		<!-- <div class="btn-group btn-group-sm pull-right" role="group" > -->
		<form class="btn-group btn-group-sm pull-right" action="ComputerList"
			method="POST">
			<input name="b1" type="submit" class="btn btn-default" value="10" />
			<input name="b2" type="submit" class="btn btn-default" value="50" />
			<input name="b3" type="submit" class="btn btn-default" value="100" />
		</form>
	</footer>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>