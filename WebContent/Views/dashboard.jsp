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
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
</head>
<body>

    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="#"> Application - Computer Database </a>
        </div>
    </header>
    
    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                <c:out value = "${numberOfElement}"/> Entries
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="Views/addComputer.jsp">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
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

                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>
                            Computer name
                        </th>
                        <th>
                            Introduced date
                        </th>
                        <!-- Table header for Discontinued Date -->
                        <th>
                            Discontinued date
                        </th>
                        <!-- Table header for Company -->
                        <th>
                            Company
                        </th>

                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                
                    <c:forEach items="${ companyWithComputers }" var="titre" varStatus="status">
                        
                        <tr>
                        <td class="editMode">
                            <input type="checkbox" name="cb" class="cb" value="0">
                        </td>
                        <td>
                            <a href="Views/editComputer.jsp" onclick=""><c:out value="${ titre.getComputer().getNom() }" /></a>
                        </td>
                        <td><c:out value="${ titre.getComputer().getIntroduced() }" /></td>
                        <td><c:out value="${ titre.getComputer().getDiscontinued() }" /></td>
                        <td><c:out value="${ titre.getCompany().getNom() }" /></td>

                    </tr>

                    </c:forEach>
              
                </tbody>
            </table>
        </div>
    </section>
    <footer class="navbar">    
        <div class="container text-center">
            <ul class="pagination">
              <li style="display:inline-block;">
                    <a href="#" aria-label="Previous">
                      <span aria-hidden="true">&laquo;</span>
                  </a>
              </li>
              <c:forEach var="i" begin="1" end="${nbPage}" step="1">
                <li style="display:inline-block;">
                    <form action = "ServletComputer" method = "GET">
	                    <input style = "display:none;" type = "text" name = "page" value = "<c:out value="${i}" />" />
	                    <input type = "submit" class="btn btn-light" value = "<c:out value="${i}" />" />
                    </form>
                </li>
              </c:forEach>
              <li style="display:inline-block;">
                <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
              </li>
            </ul>
        </div>
        
        <!-- <div class="btn-group btn-group-sm pull-right" role="group" > -->
        <form class="btn-group btn-group-sm pull-right" action="ServletComputer" method="POST">
            <input name = "b1" type="submit" class="btn btn-default" value="10"/>
            <input name = "b2" type="submit" class="btn btn-default" value="50"/>
            <input name = "b3" type="submit" class="btn btn-default" value="100"/>
        </form>
    </footer>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/dashboard.js"></script>

</body>
</html>