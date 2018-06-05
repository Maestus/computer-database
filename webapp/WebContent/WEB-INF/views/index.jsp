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
</head>
<body>

	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="pull-right navbar-brand"
				href="ServletComputer?direct=index"> Menu </a> <a
				class="navbar-brand" href="ComputerList?page=1"> Application -
				Computer Database </a>
		</div>
	</header>
	
	<section id="main">
		<div class="container">
			<h2 id="homeTitle">Menu</h2>
			<div id="actions" class="form-horizontal">
				<a class="btn btn-primary" href="ComputerList?page=1">Liste des
					computers</a>
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>